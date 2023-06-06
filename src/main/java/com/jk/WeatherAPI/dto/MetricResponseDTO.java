package com.jk.WeatherAPI.dto;


import com.jk.WeatherAPI.dto.enums.MetricType;
import com.jk.WeatherAPI.dto.enums.StatType;

import java.util.List;

public class MetricResponseDTO {
    private List<Long> sensorId;
    private MetricType metric;
    private StatType statType;
    private Double metricValue;

    public MetricResponseDTO(List<Long> sensorId, MetricType metric, StatType statType, Double metricValue) {
        this.sensorId = sensorId;
        this.metric = metric;
        this.statType = statType;
        this.metricValue = metricValue;
    }

    // Getters and setters
    public List<Long> getSensorId() {
        return sensorId;
    }

    public void setSensorId(List<Long> sensorId) {
        this.sensorId = sensorId;
    }

    public MetricType getMetric() {
        return metric;
    }

    public void setMetric(MetricType metric) {
        this.metric = metric;
    }

    public StatType getStatType() {
        return statType;
    }

    public void setStatType(StatType statType) {
        this.statType = statType;
    }

    public Double getMetricValue() {
        return metricValue;
    }

    public void setMetricValue(Double metricValue) {
        this.metricValue = metricValue;
    }

    @Override
    public String toString() {
        return "MetricResponseDTO{" +
                "sensorId=" + sensorId +
                ", metric=" + metric +
                ", statType=" + statType +
                ", metricValue=" + metricValue +
                '}';
    }
}



