package com.jk.WeatherAPI.dto;

import com.jk.WeatherAPI.dto.enums.MetricType;
import com.jk.WeatherAPI.dto.enums.StatType;

import javax.validation.constraints.NotNull;
import java.util.List;

public class StatsQueryRequestDTO {
    public final List<Long> sensorIds;

    @NotNull
    public final List<MetricType> metrics;

    @NotNull
    public final StatType statType;

    public final String startDate;

    public final String endDate;

    public final Boolean searchAllSensors;

    public StatsQueryRequestDTO(final List<Long> sensorIds, final List<MetricType> metrics, final StatType statType, final String startDate, final String endDate, final Boolean searchAllSensors) {
        this.sensorIds = sensorIds;
        this.metrics = metrics;
        this.statType = statType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.searchAllSensors = searchAllSensors;
    }

    @Override
    public String toString() {
        return "StatsQueryRequestDTO{" +
                "sensorIds=" + sensorIds +
                ", metrics=" + metrics +
                ", statType=" + statType +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", searchAllSensors=" + searchAllSensors +
                '}';
    }
}
