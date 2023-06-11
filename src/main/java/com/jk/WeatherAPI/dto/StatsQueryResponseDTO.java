package com.jk.WeatherAPI.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class StatsQueryResponseDTO {
    public final List<MetricResponseDTO> metricResponses;

    @JsonCreator
    public StatsQueryResponseDTO(@JsonProperty("metricResponses") List<MetricResponseDTO> metricResponses) {
        this.metricResponses = metricResponses;
    }

    @Override
    public String toString() {
        return "StatsQueryResponseDTO{" +
                "metricResponses=" + metricResponses +
                '}';
    }
}
