package com.jk.WeatherAPI.dto;

public class MetricDTO {
    private String metricName;
    private double metricValue;

    public MetricDTO(final String name, final double value) {
        this.metricName = name;
        this.metricValue = value;
    }

    public String getMetricName() {
        return metricName;
    }

    public void setMetricName(final String metricName) {
        this.metricName = metricName;
    }

    public double getMetricValue() {
        return metricValue;
    }

    public void setMetricValue(final double metricValue) {
        this.metricValue = metricValue;
    }

    @Override
    public String toString() {
        return "MetricDTO{" +
                "name='" + metricName + '\'' +
                ", value=" + metricValue +
                '}';
    }
}
