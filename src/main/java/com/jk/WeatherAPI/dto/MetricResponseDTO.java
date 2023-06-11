package com.jk.WeatherAPI.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jk.WeatherAPI.dto.enums.MetricType;
import com.jk.WeatherAPI.dto.enums.StatType;

import java.util.List;

public class MetricResponseDTO {
    public final MetricType metric;
    public final StatType statType;
    public final Double metricValue;

    @JsonCreator
    public MetricResponseDTO(@JsonProperty("metric") final MetricType metric, @JsonProperty("statType") final StatType statType, @JsonProperty("metricValue") Double metricValue) {
        this.metric = metric;
        this.statType = statType;
        this.metricValue = metricValue;
    }

    public MetricResponseDTO getMetricResponseDTOByType(final List<MetricResponseDTO> metricResponseDTOList, final MetricType metricType) {
        for (MetricResponseDTO metricResponseDTO : metricResponseDTOList) {
            if (metricResponseDTO.metric.equals(metricType)) {
                return metricResponseDTO;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "MetricResponseDTO{" +
                ", metric=" + metric +
                ", statType=" + statType +
                ", metricValue=" + metricValue +
                '}';
    }
}



