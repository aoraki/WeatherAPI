package com.jk.WeatherAPI.dto;

import com.jk.WeatherAPI.dto.enums.MetricType;
import com.jk.WeatherAPI.dto.enums.StatType;

import java.util.List;

public class MetricResponseDTO {
    public final MetricType metric;
    public final StatType statType;
    public final Double metricValue;

    public MetricResponseDTO(MetricType metric, StatType statType, Double metricValue) {
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



