package com.ricardoocorreia.thesis.pipelines.context;

import com.ricardoocorreia.thesis.model.commons.DeviceCommons;
import com.ricardoocorreia.thesis.model.commons.GSMA;
import com.ricardoocorreia.thesis.model.device.Device;
import com.ricardoocorreia.thesis.model.device.Direction;
import com.ricardoocorreia.thesis.model.measurement.DeviceMeasurement;
import org.apache.beam.runners.flink.FlinkRunner;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.kafka.KafkaIO;
import org.apache.beam.sdk.io.kafka.KafkaRecord;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
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
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class DeviceContextProviderPipeline {

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
                .apply(MapElements.into(TypeDescriptors.kvs(TypeDescriptors.strings(), TypeDescriptor.of(Device.class)))
                        .via((ProcessFunction<KV<String, String>, Device>) input -> {
                            final JsonNode jsonPayload = getJsonPayload(objectMapper, input);
                            final String deviceId = jsonPayload.get(ID_FIELD_NAME).asText();
                            final Stream<Map.Entry<String, JsonNode>> remainingFields =
                                    getRemainingFields(jsonPayload);

                            return Device.builder()
                                    .batteryLevel(1.0) // Needs to be added on orion
                                    .dateFirstUsed(Instant.now())
                                    .dateObserved(Instant.now())
                                    .dateInstalled(Instant.now())
                                    .dateLastCalibration(Instant.now())
                                    .dateLastValueReported(Instant.now())
                                    .dateManufactured(Instant.now())
                                    .deviceCommons(DeviceCommons.builder()
                                            .category(List.of()) // Check this
                                            .macAddress("") // Needs to checked
                                            .supportedProtocol(List.of()) // Also needs to be checked
                                            .build())
                                    .gsma(GSMA.builder()
                                            .id(URI.create(""))
                                            .dateModified(Instant.now())
                                            .dateCreated(Instant.now())
                                            .description("")
                                            .name("")
                                            .build())
                                    .deviceState("") // Check it
                                    .direction(Direction.ENTRY)
                                    .depth(0.0)
                                    .distance(0.0)
                                    .dstAware(false)
                                    .build();
                        }))
                .apply(MapElements.into(TypeDescriptors.kvs(TypeDescriptors.strings(), TypeDescriptors.strings()))
                        .via((ProcessFunction<KV<String, DeviceMeasurement>, KV<String, String>>) input -> KV.of(input.getKey(), objectMapper.writeValueAsString(input.getValue()))))
                .apply(KafkaIO.<String, String>write()
                        .withBootstrapServers("localhost:9092")
                        .withTopic("devices")
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
                .filter(field -> !field.getKey().equals(ID_FIELD_NAME));
    }

}
