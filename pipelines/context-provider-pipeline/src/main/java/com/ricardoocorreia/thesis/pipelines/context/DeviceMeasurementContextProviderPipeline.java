package com.ricardoocorreia.thesis.pipelines.context;

import com.ricardoocorreia.thesis.model.commons.GSMA;
import com.ricardoocorreia.thesis.model.measurement.DeviceMeasurement;
import org.apache.beam.runners.flink.FlinkRunner;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.kafka.KafkaIO;
import org.apache.beam.sdk.io.kafka.KafkaRecord;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.FlatMapElements;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.ProcessFunction;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.TypeDescriptor;
import org.apache.beam.sdk.values.TypeDescriptors;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.time.Instant;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class DeviceMeasurementContextProviderPipeline {

    public static final String ID_FIELD_NAME = "id";
    public static final String TYPE_FIELD_NAME = "type";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {

        final PipelineOptions options = PipelineOptionsFactory.create();
        options.setRunner(FlinkRunner.class);

        final Pipeline pipeline = Pipeline.create(options);

        pipeline.apply(KafkaIO.<String, String>read()
                        .withBootstrapServers("localhost:9092")
                        .withTopic("test-topic")
                        .withKeyDeserializer(StringDeserializer.class)
                        .withValueDeserializer(StringDeserializer.class)
                        .withConsumerConfigUpdates(Map.of("group.id", "context_provider_pipeline")))
                .apply(MapElements.into(TypeDescriptors.kvs(TypeDescriptors.strings(), TypeDescriptors.strings()))
                        .via((ProcessFunction<KafkaRecord<String, String>, KV<String, String>>) input -> KV.of(input.getKV().getKey(), input.getKV().getValue())))
                .apply(FlatMapElements.into(TypeDescriptors.kvs(TypeDescriptors.strings(), TypeDescriptor.of(DeviceMeasurement.class)))
                        .via((ProcessFunction<KV<String, String>, Iterable<KV<String, DeviceMeasurement>>>) input -> {
                            final JsonNode jsonPayload = getJsonPayload(objectMapper, input);
                            final String deviceId = jsonPayload.get(ID_FIELD_NAME).asText();
                            final String deviceType = jsonPayload.get(TYPE_FIELD_NAME).asText();
                            final Stream<Map.Entry<String, JsonNode>> remainingFields =
                                    getRemainingFields(jsonPayload);

                            return remainingFields.map(field -> DeviceMeasurement.builder()
                                            .gsma(GSMA.builder()
                                                    .id(URI.create("urn:ngsi-ld:MEASUREMENT:id:PMZY:" + Math.random()))
                                                    .dateCreated(Instant.now())
                                                    .dateModified(Instant.now())
                                                    .owner(List.of(
                                                            URI.create("urn:ngsi-ld:MEASUREMENT:refDevice:ZMHH:" + deviceId)
                                                    ))
                                                    .build())
                                            .dateObserved(Instant.now())
                                            .measurementType(field.getKey())
                                            .numValue(field.getValue().get("value").asDouble())
                                            .textValue(field.getValue().get("value").asText())
                                            .deviceType(deviceType)
                                            .refDevice(URI.create("urn:ngsi-ld:MEASUREMENT:refDevice:ZMHH:" + deviceId))
                                            .build())
                                    .map(deviceMeasurement -> KV.of(deviceId, deviceMeasurement))
                                    .collect(Collectors.toUnmodifiableList());
                        }))
                .apply(MapElements.into(TypeDescriptors.kvs(TypeDescriptors.strings(), TypeDescriptors.strings()))
                        .via((ProcessFunction<KV<String, DeviceMeasurement>, KV<String, String>>) input -> KV.of(input.getKey(), objectMapper.writeValueAsString(input.getValue()))))
                .apply(KafkaIO.<String, String>write()
                        .withBootstrapServers("localhost:9092")
                        .withTopic("device-measurements")
                        .withKeySerializer(StringSerializer.class)
                        .withValueSerializer(StringSerializer.class));

        pipeline.run().waitUntilFinish();
    }

    private static JsonNode getJsonPayload(ObjectMapper objectMapper, KV<String, String> input) throws IOException {
        final String payload = input.getValue();
        return objectMapper.readTree(payload);
    }

    private static Stream<Map.Entry<String, JsonNode>> getRemainingFields(JsonNode jsonPayload) {
        final Iterator<Map.Entry<String, JsonNode>> fields = jsonPayload.getFields();
        return StreamSupport.stream(
                        Spliterators.spliteratorUnknownSize(fields, 0), false)
                .filter(field -> !field.getKey().equals(ID_FIELD_NAME) && !field.getKey().equals(TYPE_FIELD_NAME));
    }

}
