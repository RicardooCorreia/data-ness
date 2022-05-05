package com.ricardoocorreia.thesis.contextbrokerkafkaadapter.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ricardoocorreia.thesis.contextbrokerkafkaadapter.domain.PublishContextChange;
import lombok.SneakyThrows;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public record KafkaPublisher(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {

    @SneakyThrows
    public void publish(String key, Object payload, String topic) {

        final String serializedPayload = objectMapper.writeValueAsString(payload);
        kafkaTemplate.send(topic, key, serializedPayload);
    }
}
