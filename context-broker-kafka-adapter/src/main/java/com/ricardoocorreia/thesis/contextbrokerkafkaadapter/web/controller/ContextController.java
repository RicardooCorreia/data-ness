package com.ricardoocorreia.thesis.contextbrokerkafkaadapter.web.controller;

import com.ricardoocorreia.thesis.contextbrokerkafkaadapter.domain.PublishContextChange;
import com.ricardoocorreia.thesis.contextbrokerkafkaadapter.domain.catalog.ContextUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ContextController {

    private final PublishContextChange publishContextChange;

    @PostMapping("api/v1/context")
    public void contextUpdate(
            @RequestHeader Map<String, String> headers,
            @RequestBody ContextUpdate payload) {

        log.info(String.valueOf(headers.entrySet()));
        log.info(payload.toString());

        publishContextChange.publish(payload);
    }
}
