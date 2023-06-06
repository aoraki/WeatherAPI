package com.jk.WeatherAPI.dto;

import java.util.List;

public class SensorDataDTO {
    private Long sensorId;
    private List<MetricDTO> metrics;

    public SensorDataDTO() {
    }

    public SensorDataDTO(Long sensorId, List<MetricDTO> metrics) {
        this.sensorId = sensorId;
        this.metrics = metrics;
    }

    public Long getSensorId() {
        return sensorId;
    }

    public void setSensorId(Long sensorId) {
        this.sensorId = sensorId;
    }

    public List<MetricDTO> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<MetricDTO> metrics) {
        this.metrics = metrics;
    }

    @Override
    public String toString() {
        return "SensorDataDTO{" +
                "sensorId=" + sensorId +
                ", metrics=" + metrics +
                '}';
    }
}
