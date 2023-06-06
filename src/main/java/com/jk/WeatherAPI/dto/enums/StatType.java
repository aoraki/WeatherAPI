package com.jk.WeatherAPI.dto.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.sun.istack.NotNull;

import java.util.Arrays;

public enum StatType {
    MINIMUM("MIN"),
    MAXIMUM("MAX"),
    SUM ("SUM"),
    AVERAGE("AVG");

    public final String value;

    StatType(@NotNull final String value) {
        this.value = value;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static StatType fromString(final String value) {
        return Arrays.stream(StatType.values())
                .filter(c -> c.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown state " + value));
    }
}
