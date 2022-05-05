package com.ricardoocorreia.thesis.model.device;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.ricardoocorreia.thesis.model.commons.DeviceCommons;
import com.ricardoocorreia.thesis.model.commons.GSMA;
import lombok.Builder;
import lombok.Value;

import java.net.URI;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Value
@Builder
public class Device {

    Type type = Type.DEVICE;

    List<String> controlledAsset;

    String mnc;

    String mcc;

    List<String> ipAddress;

    List<Configuration> configuration;

    Instant dateInstalled;

    Instant dateFirstUsed;

    Instant dateManufactured;

    String hardwareVersion;

    String softwareVersion;

    String firmwareVersion;

    String osVersion;

    Double distance;

    Boolean dstAware;

    Double depth;

    String relativePosition;

    Direction direction;

    Instant dateLastCalibration;

    String serialNumber;

    String provider;

    URI refDeviceModel;

    Double batteryLevel;

    Double rssi;

    String deviceState;

    Instant dateLastValueReported;

    String value;

    Instant dateObserved;

    Map<String, String> metadata = new HashMap<>();

    @JsonUnwrapped
    DeviceCommons deviceCommons;

    @JsonUnwrapped
    GSMA gsma;
}
