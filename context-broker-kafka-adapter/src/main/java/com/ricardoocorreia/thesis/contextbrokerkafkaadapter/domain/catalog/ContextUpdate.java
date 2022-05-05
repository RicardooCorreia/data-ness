package com.ricardoocorreia.thesis.contextbrokerkafkaadapter.domain.catalog;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record ContextUpdate(String subscriptionId, List<Map<String, Object>> data) {

    @Value
    public static class Data {
        String id;
        String type;
        Map<String, String> fields = new HashMap<>();

        @JsonAnyGetter
        public Map<String, String> getFields() {
            return fields;
        }

        @JsonAnySetter
        public void setFields(String name, String value) {
            fields.put(name, value);
        }
    }
}
