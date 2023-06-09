package com.jk.WeatherAPI.dto;

import java.util.List;

public class StatsQueryResponseDTO {
    public final List<MetricResponseDTO> metricResponses;

    public StatsQueryResponseDTO(List<MetricResponseDTO> metricResponses) {
        this.metricResponses = metricResponses;
    }

    @Override
    public String toString() {
        return "StatsQueryResponseDTO{" +
                "metricResponses=" + metricResponses +
                '}';
    }
}
