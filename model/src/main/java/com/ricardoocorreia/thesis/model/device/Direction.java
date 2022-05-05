package com.ricardoocorreia.thesis.model.device;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Direction {
    INLET("Inlet"),
    OUTLET("Outlet"),
    ENTRY("Entry"),
    EXIT("Exit");

    private final String value;
}
