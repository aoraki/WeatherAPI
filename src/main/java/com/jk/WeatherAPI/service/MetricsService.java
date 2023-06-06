package com.jk.WeatherAPI.service;

import com.jk.WeatherAPI.dto.SensorDataDTO;
import com.jk.WeatherAPI.dto.StatsQueryRequestDTO;
import com.jk.WeatherAPI.dto.StatsQueryResponseDTO;

public interface MetricsService {

    StatsQueryResponseDTO getStats(final StatsQueryRequestDTO query);
    boolean submitMetrics(final SensorDataDTO metricData);

}
