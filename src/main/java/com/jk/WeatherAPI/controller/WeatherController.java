package com.jk.WeatherAPI.controller;

import com.jk.WeatherAPI.dto.SensorDataDTO;
import com.jk.WeatherAPI.dto.StatsQueryRequestDTO;
import com.jk.WeatherAPI.dto.StatsQueryResponseDTO;
import com.jk.WeatherAPI.service.MetricsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
    public ResponseEntity<StatsQueryResponseDTO> queryMetrics(@RequestBody @Valid final StatsQueryRequestDTO statsQuery) {
        return ResponseEntity.ok(this.metricsService.getStats(statsQuery));
    }

    @PostMapping("/metrics")
    public ResponseEntity<SensorDataDTO> createSample(@RequestBody @Valid final SensorDataDTO sensorData) {
        final SensorDataDTO sensorDataDTO = metricsService.createSample(sensorData);
        return new ResponseEntity(sensorDataDTO, HttpStatus.CREATED);
    }

    @GetMapping("/metrics")
    public ResponseEntity<List<SensorDataDTO>> getAllSamples() {
        final List<SensorDataDTO> sensorDataDTO = metricsService.getAllSamples();
        return new ResponseEntity(sensorDataDTO, HttpStatus.OK);
    }

    @GetMapping("/metrics/{sampleId}")
    public ResponseEntity<SensorDataDTO> getSampleById(@PathVariable final Long sampleId) {
        final SensorDataDTO sensorDataDTO = metricsService.getSampleBySampleId(sampleId);
        return new ResponseEntity(sensorDataDTO, HttpStatus.OK);
    }

    @DeleteMapping("/metrics/{sampleId}")
    public ResponseEntity<Boolean> deleteSampleById(@PathVariable final Long sampleId) {
        final Boolean deleted = metricsService.deleteSample(sampleId);
        return new ResponseEntity(deleted, HttpStatus.OK);
    }

    @PatchMapping("/metrics")
    public ResponseEntity<SensorDataDTO> upateSample(@RequestBody @Valid final SensorDataDTO sensorData) {
        final SensorDataDTO sensorDataDTO = metricsService.updateSample(sensorData);
        return new ResponseEntity(sensorDataDTO, HttpStatus.OK);
    }
}
