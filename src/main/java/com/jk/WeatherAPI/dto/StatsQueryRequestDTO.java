package com.jk.WeatherAPI.dto;

import com.jk.WeatherAPI.dto.enums.MetricType;
import com.jk.WeatherAPI.dto.enums.StatType;

import java.util.List;

public class StatsQueryRequestDTO {
    private List<Long> sensorIds;
    private List<MetricType> metrics;
    private StatType statType;
    private String startDate;
    private String endDate;

    public StatsQueryRequestDTO() {
    }

    public StatsQueryRequestDTO(List<Long> sensorIds, List<MetricType> metrics, StatType statType, String startDate, String endDate) {
        this.sensorIds = sensorIds;
        this.metrics = metrics;
        this.statType = statType;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public List<Long> getSensorIds() {
        return sensorIds;
    }

    public void setSensorIds(List<Long> sensorIds) {
        this.sensorIds = sensorIds;
    }

    public List<MetricType> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<MetricType> metrics) {
        this.metrics = metrics;
    }

    public StatType getStatType() {
        return statType;
    }

    public void setStatType(StatType statType) {
        this.statType = statType;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "StatsQueryRequestDTO{" +
                "sensorIds=" + sensorIds +
                ", metrics=" + metrics +
                ", statType=" + statType +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                '}';
    }
}
