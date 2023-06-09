package com.jk.WeatherAPI.dto;

import java.util.List;
import java.util.Objects;

public class MetricDTO {

    public final String metricName;
    public final double metricValue;

    public MetricDTO(final String name, final double value) {
        this.metricName = name;
        this.metricValue = value;
    }

    public MetricDTO getMetricByName(final List<MetricDTO> metricList, final String metricName) {
        for (MetricDTO obj : metricList) {
            if (obj.metricName == metricName) {
                return obj;
            }
        }
        return null;
    }


    @Override
    public String toString() {
        return "MetricDTO{" +
                "name='" + metricName + '\'' +
                ", value=" + metricValue +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetricDTO metricDTO = (MetricDTO) o;
        return Objects.equals(metricName, metricDTO.metricName);
    }
}
