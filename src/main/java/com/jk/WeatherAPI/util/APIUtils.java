package com.jk.WeatherAPI.util;

import com.jk.WeatherAPI.dto.MetricDTO;
import com.jk.WeatherAPI.dto.SensorDataDTO;
import com.jk.WeatherAPI.dto.enums.MetricType;
import com.jk.WeatherAPI.entities.SensorData;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.List;

public class APIUtils {

    public SensorData convertDTOToSensorData(final SensorDataDTO sensorData) {
        Double temperature = getMetricValue(sensorData, MetricType.TEMPERATURE);
        Double rainfall = getMetricValue(sensorData, MetricType.RAINFALL);
        Double windspeed = getMetricValue(sensorData, MetricType.WINDSPEED);
        Double humidity = getMetricValue(sensorData, MetricType.HUMIDITY);
        return new SensorData(sensorData.sampleId, sensorData.sensorId, Timestamp.valueOf(LocalDateTime.now()), temperature, rainfall, windspeed, humidity);
    }

    public SensorDataDTO convertSensorDataToDTO(final SensorData sensorData) {
        List<MetricDTO> metricDTOs = new ArrayList<>();
        metricDTOs.add(this.generateMetricDTO(MetricType.TEMPERATURE, sensorData.getTemperature()));
        metricDTOs.add(this.generateMetricDTO(MetricType.WINDSPEED, sensorData.getWindspeed()));
        metricDTOs.add(this.generateMetricDTO(MetricType.HUMIDITY, sensorData.getHumidity()));
        metricDTOs.add(this.generateMetricDTO(MetricType.RAINFALL, sensorData.getRainfall()));
        return new SensorDataDTO(sensorData.getSampleId(), sensorData.getSensorId(), metricDTOs);
    }

    private MetricDTO generateMetricDTO(final MetricType metricType, final Double metricValue){
        return new MetricDTO(metricType, metricValue);
    }

    public Double getMetricValue(final SensorDataDTO sensorData, final MetricType metricType) {
        return sensorData.metrics.stream()
                .filter(metric -> metric.metricType.equals(metricType))
                .map(metric -> metric.metricValue)
                .findFirst()
                .orElse(null);
    }
}
