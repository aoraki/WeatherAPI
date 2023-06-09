package com.jk.WeatherAPI.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.util.List;

public class SensorDataDTO {
    @NotNull
    public final Long sampleId;

    @NotNull
    public final Long sensorId;

    @Valid
    @NotEmpty
    @NotNull
    public final List<@Valid MetricDTO> metrics;

    public SensorDataDTO(final Long sampleId, final Long sensorId, final List<MetricDTO> metrics) {
        this.sampleId = sampleId;
        this.sensorId = sensorId;
        this.metrics = metrics;
    }

    @Override
    public String toString() {
        return "SensorDataDTO{" +
                "sampleId=" + sampleId +
                "sensorId=" + sensorId +
                ", metrics=" + metrics +
                '}';
    }
}
