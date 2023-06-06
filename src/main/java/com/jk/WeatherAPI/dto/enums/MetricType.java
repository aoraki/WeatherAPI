package com.jk.WeatherAPI.dto.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public enum MetricType {
    TEMPERATURE("TEMP"),
    RAINFALL("RAIN"),
    HUMIDITY("HUM"),
    WINDSPEED("WIND");

    public final String value;

    MetricType(final String value) {
        this.value = value;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static MetricType fromString(final String value) {
        return Arrays.stream(MetricType.values())
                .filter(c -> c.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown state " + value));
    }
}
