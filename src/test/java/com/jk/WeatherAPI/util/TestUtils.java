package com.jk.WeatherAPI.util;

import com.jk.WeatherAPI.dto.MetricDTO;
import com.jk.WeatherAPI.dto.SensorDataDTO;
import com.jk.WeatherAPI.dto.enums.MetricType;
import com.jk.WeatherAPI.entities.SensorData;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class TestUtils {

    public SensorDataDTO generateSensorDataDTO(final Long sampleId, final Long sensorId) {
        List<MetricDTO> metrics = generateMetricsDTO();
        return new SensorDataDTO(sampleId, sensorId, metrics);
    }

    private List<MetricDTO> generateMetricsDTO() {
        List<MetricDTO> metrics = new ArrayList<>();
        metrics.add(new MetricDTO(MetricType.TEMPERATURE.value, 10.0));
        metrics.add(new MetricDTO(MetricType.HUMIDITY.value, 20.0));
        return metrics;
    }

    public SensorData generateSensorData(final Long sampleId, final Long sensorId, final Timestamp sampleTime, final double temperature, final double rainfall, final double windspeed, final double humidity){
        return new SensorData(sampleId, sensorId, sampleTime, temperature, rainfall, windspeed, humidity);
    }

}
