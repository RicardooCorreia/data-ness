package com.ricardoocorreia.thesis.contextbrokerkafkaadapter.domain;

import com.google.common.base.CaseFormat;
import com.ricardoocorreia.thesis.contextbrokerkafkaadapter.domain.catalog.ContextUpdate;
import com.ricardoocorreia.thesis.contextbrokerkafkaadapter.kafka.KafkaPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public record PublishContextChange(KafkaPublisher kafkaPublisher) {

    public void publish(ContextUpdate payload) {

        final Tuple tuple = Optional.ofNullable(payload.data())
                .map(data -> data.get(0))
                .map(data -> new Tuple(String.valueOf(data.get("id")), String.valueOf(data.get("type"))))
                .orElseThrow(() -> new RuntimeException("No id found for payload: " + payload));

        final String topic = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, tuple.type);
        kafkaPublisher.publish(tuple.key, payload.data(), topic);
    }

    private record Tuple(String key, String type) {}
}
