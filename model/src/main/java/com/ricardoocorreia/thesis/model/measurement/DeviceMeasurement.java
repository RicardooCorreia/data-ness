package com.ricardoocorreia.thesis.model.measurement;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.ricardoocorreia.thesis.model.commons.GSMA;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.net.URI;
import java.time.Instant;

@Value
@Builder
public class DeviceMeasurement implements Serializable {

    Type type = Type.DEVICE_MEASUREMENT;

    Double numValue;

    String textValue;

    String controlledProperty;

    URI refDevice;

    String deviceType;

    String measurementType;

    Instant dateObserved;

    Boolean outlier;

    String unit;

    @JsonUnwrapped
    GSMA gsma;
}
