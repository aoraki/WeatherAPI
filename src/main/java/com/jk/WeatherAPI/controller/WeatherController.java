package com.jk.WeatherAPI.controller;

import com.jk.WeatherAPI.dto.SensorDataDTO;
import com.jk.WeatherAPI.dto.StatsQueryRequestDTO;
import com.jk.WeatherAPI.dto.StatsQueryResponseDTO;
import com.jk.WeatherAPI.service.MetricsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/weather")
public class WeatherController {

    private final MetricsService metricsService;

    public WeatherController(final MetricsService metricsService) {
        this.metricsService = metricsService;
    }

    /**
     * Use of an non-idempotent HTTP method seems a bit off for a request to retrieve sensor stats considering no state is being muteted in the backend.
     * However, I wanted to utilise a query object being passed in the payload of the request
     */
    @PostMapping("/stats")
    public ResponseEntity<StatsQueryResponseDTO> getSensorMetrics(@RequestBody StatsQueryRequestDTO statsQuery) {
        return ResponseEntity.ok(this.metricsService.getStats(statsQuery));
    }

    @PostMapping("/metrics")
    public ResponseEntity<?> addMetric(@RequestBody SensorDataDTO sensorData) {
        metricsService.submitMetrics(sensorData);
        return ResponseEntity.ok("Metric added successfully");
    }
}
