package com.jk.WeatherAPI;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jk.WeatherAPI.dto.SensorDataDTO;
import com.jk.WeatherAPI.dto.StatsQueryRequestDTO;
import com.jk.WeatherAPI.dto.StatsQueryResponseDTO;
import com.jk.WeatherAPI.dto.enums.MetricType;
import com.jk.WeatherAPI.dto.enums.StatType;
import com.jk.WeatherAPI.util.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class WeatherAPIIntegrationTests {

    private final TestUtils testUtils = new TestUtils();

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void createSensorDataReading200Response(){
        final ResponseEntity<SensorDataDTO> createWorkOrderResponse = createSensorData(1000L, 1L);
        final SensorDataDTO sensorDateDTO = createWorkOrderResponse.getBody();

        assertThat(createWorkOrderResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(sensorDateDTO.metrics.size()).isEqualTo(4);
        assertThat(sensorDateDTO.sampleId).isEqualTo(1000);
        assertThat(sensorDateDTO.sensorId).isEqualTo(1);
        assertThat(sensorDateDTO.metrics.get(0).getMetricByName(sensorDateDTO.metrics, MetricType.TEMPERATURE).metricType).isEqualTo(MetricType.TEMPERATURE);
        assertThat(sensorDateDTO.metrics.get(0).getMetricByName(sensorDateDTO.metrics, MetricType.TEMPERATURE).metricValue).isEqualTo(10);
        assertThat(sensorDateDTO.metrics.get(0).getMetricByName(sensorDateDTO.metrics, MetricType.HUMIDITY).metricType).isEqualTo(MetricType.HUMIDITY);
        assertThat(sensorDateDTO.metrics.get(0).getMetricByName(sensorDateDTO.metrics, MetricType.HUMIDITY).metricValue).isEqualTo(20);
        assertThat(sensorDateDTO.metrics.get(0).getMetricByName(sensorDateDTO.metrics, MetricType.RAINFALL).metricType).isEqualTo(MetricType.RAINFALL);
        assertThat(sensorDateDTO.metrics.get(0).getMetricByName(sensorDateDTO.metrics, MetricType.RAINFALL).metricValue).isNull();
        assertThat(sensorDateDTO.metrics.get(0).getMetricByName(sensorDateDTO.metrics, MetricType.WINDSPEED).metricType).isEqualTo(MetricType.WINDSPEED);
        assertThat(sensorDateDTO.metrics.get(0).getMetricByName(sensorDateDTO.metrics, MetricType.WINDSPEED).metricValue).isNull();
    }

    @Test
    void createSensorDataReading409Response(){
        final ResponseEntity<SensorDataDTO> createWorkOrderResponse = createSensorData(1000L, 1L);
        assertThat(createWorkOrderResponse.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void getSensorDataBySampleId200Response(){
        final ResponseEntity<SensorDataDTO> getSensorDataResponse = restTemplate.getForEntity("/v1/weather/metrics/{sampleId}",
                SensorDataDTO.class, 1000);
        final SensorDataDTO sensorDateDTO = getSensorDataResponse.getBody();

        assertThat(getSensorDataResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(sensorDateDTO.metrics.size()).isEqualTo(4);
        assertThat(sensorDateDTO.sampleId).isEqualTo(1000);
        assertThat(sensorDateDTO.sensorId).isEqualTo(1);
        assertThat(sensorDateDTO.metrics.get(0).getMetricByName(sensorDateDTO.metrics, MetricType.TEMPERATURE).metricType).isEqualTo(MetricType.TEMPERATURE);
        assertThat(sensorDateDTO.metrics.get(0).getMetricByName(sensorDateDTO.metrics, MetricType.TEMPERATURE).metricValue).isEqualTo(10);
        assertThat(sensorDateDTO.metrics.get(0).getMetricByName(sensorDateDTO.metrics, MetricType.HUMIDITY).metricType).isEqualTo(MetricType.HUMIDITY);
        assertThat(sensorDateDTO.metrics.get(0).getMetricByName(sensorDateDTO.metrics, MetricType.HUMIDITY).metricValue).isEqualTo(20);
        assertThat(sensorDateDTO.metrics.get(0).getMetricByName(sensorDateDTO.metrics, MetricType.RAINFALL).metricType).isEqualTo(MetricType.RAINFALL);
        assertThat(sensorDateDTO.metrics.get(0).getMetricByName(sensorDateDTO.metrics, MetricType.RAINFALL).metricValue).isNull();
        assertThat(sensorDateDTO.metrics.get(0).getMetricByName(sensorDateDTO.metrics, MetricType.WINDSPEED).metricType).isEqualTo(MetricType.WINDSPEED);
        assertThat(sensorDateDTO.metrics.get(0).getMetricByName(sensorDateDTO.metrics, MetricType.WINDSPEED).metricValue).isNull();
    }

    @Test
    void getSensorDataBySampleId404Response(){
        final ResponseEntity<SensorDataDTO> getSensorDataResponse = restTemplate.getForEntity("/v1/weather/metrics/{sampleId}",
                SensorDataDTO.class, 10000);
        assertThat(getSensorDataResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteSensorDataBySampleIdSuccess(){
        createSensorData(500L, 1L);
        final ResponseEntity<String> response = restTemplate.exchange("/v1/weather/metrics/500", HttpMethod.DELETE, null, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void deleteSensorDataBySampleId404Response(){
        final ResponseEntity<String> response = restTemplate.exchange("/v1/weather/metrics/501", HttpMethod.DELETE, null, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    
    private ResponseEntity<SensorDataDTO> createSensorData(final Long sampleId, final Long sensorId) {
        final SensorDataDTO request = testUtils.generateSensorDataDTO(sampleId, sensorId);
        return restTemplate.postForEntity("/v1/weather/metrics", request, SensorDataDTO.class);
    }
}
