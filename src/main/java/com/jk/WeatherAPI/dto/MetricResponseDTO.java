package com.jk.WeatherAPI.dto;

import com.jk.WeatherAPI.dto.enums.MetricType;
import com.jk.WeatherAPI.dto.enums.StatType;

import java.util.List;

public class MetricResponseDTO {
    public final List<Long> sensorId;
    public final  MetricType metric;
    public final  StatType statType;
    public final  Double metricValue;

    public MetricResponseDTO(List<Long> sensorId, MetricType metric, StatType statType, Double metricValue) {
        this.sensorId = sensorId;
        this.metric = metric;
        this.statType = statType;
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



