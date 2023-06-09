package com.jk.WeatherAPI.service;

import com.jk.WeatherAPI.dto.SensorDataDTO;
import com.jk.WeatherAPI.dto.StatsQueryRequestDTO;
import com.jk.WeatherAPI.dto.StatsQueryResponseDTO;

import java.util.List;

public interface MetricsService {

    // For metric data queries
    StatsQueryResponseDTO getStats(final StatsQueryRequestDTO query);

    // For CRUD operations on records within the sensor_data table
    SensorDataDTO createSample(final SensorDataDTO sensorData);
    SensorDataDTO updateSample(final SensorDataDTO sensorData);
    boolean deleteSample(final Long sampleId);
    SensorDataDTO getSampleBySampleId(final Long sampleId);
    List<SensorDataDTO> getAllSamples();
}
