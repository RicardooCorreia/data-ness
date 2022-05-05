package com.ricardoocorreia.thesis.model.commons;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SupportedProtocolType {
    THREE_G("3g"),
    BLUETOOTH("bluetooth"),
    BLUETOOTH_LE("bluetooth LE"),
    CAT_M("cat-m"),
    COAP("coap"),
    EC_GSM_IOT("ec-gsm-iot"),
    GPRS("gprs"),
    HTTP("http"),
    LWM2M("lwm2m"),
    LORA("lora"),
    LTE_M("lte-m"),
    MQTT("mqtt"),
    NB_IOT("nb-iot"),
    ONEM2M("onem2m"),
    SIGFOX("sigfox"),
    UL20("ul20"),
    WEBSOCKET("websocket");

    private final String value;
}
