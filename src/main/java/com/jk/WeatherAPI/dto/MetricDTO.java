package com.jk.WeatherAPI.dto;

import com.jk.WeatherAPI.dto.enums.MetricType;

import java.util.List;
import java.util.Objects;

public class MetricDTO {

    public final MetricType metricType;
    public final Double metricValue;

    public MetricDTO(final MetricType metricType, final Double value) {
        this.metricType = metricType;
        this.metricValue = value;
    }

    public MetricDTO getMetricByName(final List<MetricDTO> metricList, final MetricType metricType) {
        for (MetricDTO metricDTO : metricList) {
            if (metricDTO.metricType.equals(metricType)) {
                return metricDTO;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "MetricDTO{" +
                "type='" + metricType + '\'' +
                ", value=" + metricValue +
                '}';
    }

    /*
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetricDTO metricDTO = (MetricDTO) o;
        return Objects.equals(metricType, metricDTO.metricType);
    }*/
}
