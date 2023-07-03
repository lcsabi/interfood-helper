package com.lcsdev.interfoodhelper.util;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ResponseStatus {

    PROCESSING("processing"),
    COMPLETED("completed");

    private final String value;

    ResponseStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}