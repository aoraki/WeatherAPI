package com.jk.WeatherAPI.util;

import com.jk.WeatherAPI.dto.*;
import com.jk.WeatherAPI.dto.enums.MetricType;
import com.jk.WeatherAPI.dto.enums.StatType;
import com.jk.WeatherAPI.entities.SensorData;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestUtils {

    public StatsQueryRequestDTO generateStatsQueryRequestDTO(final List<Long> sensorIds, final List<MetricType> metricTypes, final StatType statType, final String startDate, final String endDate, final Boolean searchAllSensors) {
        return new StatsQueryRequestDTO(sensorIds, metricTypes, statType, startDate, endDate, searchAllSensors);
    }

    public StatsQueryResponseDTO generateStatsQueryResponseDTO() {
        return new StatsQueryResponseDTO(Arrays.asList(generateMetricResponseDTO(MetricType.TEMPERATURE, StatType.AVG, 40.0), generateMetricResponseDTO(MetricType.RAINFALL, StatType.AVG, 20.0)));
    }

    public MetricResponseDTO generateMetricResponseDTO(final MetricType metricType, final StatType statType, final Double metricValue){
        return new MetricResponseDTO(metricType, statType,metricValue);
    }

    public SensorDataDTO generateSensorDataDTO(final Long sampleId, final Long sensorId) {
        List<MetricDTO> metrics = generateMetricsDTO();
        return new SensorDataDTO(sampleId, sensorId, metrics);
    }

    public SensorDataDTO generateInvalidSensorDataDTO(final Long sampleId, final Long sensorId) {
        List<MetricDTO> metrics = generateInvalidMetricsDTO();
        return new SensorDataDTO(sampleId, sensorId, metrics);
    }

    private List<MetricDTO> generateInvalidMetricsDTO() {
        List<MetricDTO> metrics = new ArrayList<>();
        metrics.add(new MetricDTO(null, 10.0));
        metrics.add(new MetricDTO(MetricType.HUMIDITY, null));
        return metrics;
    }

    private List<MetricDTO> generateMetricsDTO() {
        List<MetricDTO> metrics = new ArrayList<>();
        metrics.add(new MetricDTO(MetricType.TEMPERATURE, 10.0));
        metrics.add(new MetricDTO(MetricType.HUMIDITY, 20.0));
        return metrics;
    }

    public SensorData generateSensorData(final Long sampleId, final Long sensorId, final Timestamp sampleTime, final double temperature, final double rainfall, final double windspeed, final double humidity){
        return new SensorData(sampleId, sensorId, sampleTime, temperature, rainfall, windspeed, humidity);
    }
}
