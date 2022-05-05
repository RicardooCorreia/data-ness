package com.ricardoocorreia.thesis.model.commons;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class DeviceCommons {

    List<ControlledProperty> controlledProperty;

    List<CategoryType> category;

    List<SupportedProtocolType> supportedProtocol;

    String macAddress;
}
