package com.jk.WeatherAPI.dto;

import java.util.List;

public class StatsQueryResponseDTO {

    private List<MetricResponseDTO> metricResponses;

    public StatsQueryResponseDTO(List<MetricResponseDTO> metricResponses) {
        this.metricResponses = metricResponses;
    }

    public List<MetricResponseDTO> getMetricResponses() {
        return metricResponses;
    }

    public void setMetricResponses(List<MetricResponseDTO> metricResponses) {
        this.metricResponses = metricResponses;
    }

    @Override
    public String toString() {
        return "StatsQueryResponseDTO{" +
                "metricResponses=" + metricResponses +
                '}';
    }
}
