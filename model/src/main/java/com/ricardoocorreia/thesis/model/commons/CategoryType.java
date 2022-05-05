package com.ricardoocorreia.thesis.model.commons;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CategoryType {
    ACTUATOR("actuator"),
    BEACON("beacon"),
    ENDGUN("endgun"),
    HVAC("HVAC"),
    IMPLEMENT("implement"),
    IRR_SECTION("irrSection"),
    IRR_SYSTEM("irrSystem"),
    METER("meter"),
    MULTIMEDIA("multimedia"),
    NETWORK("network"),
    SENSOR("sensor");

    private final String value;
}
