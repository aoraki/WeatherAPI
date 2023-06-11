package com.jk.WeatherAPI;

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
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

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

    @Test
    void queryMetricsSuccessAverageStatsOne(){
        final StatsQueryRequestDTO statsQueryRequestDTO = testUtils.generateStatsQueryRequestDTO(Arrays.asList(1L), StatType.AVG, false, true, false, false, false, "2023-05-01", "2023-05-30");

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<StatsQueryRequestDTO> requestEntity = new HttpEntity<>(statsQueryRequestDTO, headers);

        final ResponseEntity<StatsQueryResponseDTO> response = restTemplate.postForEntity(
                "/v1/weather/stats",
                requestEntity,
                StatsQueryResponseDTO.class
        );

        final StatsQueryResponseDTO statsQueryResponseDTO = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(statsQueryResponseDTO.metricResponses.size()).isEqualTo(1);
        assertThat(statsQueryResponseDTO.metricResponses.get(0).getMetricResponseDTOByType(statsQueryResponseDTO.metricResponses, MetricType.TEMPERATURE).metric).isEqualTo(MetricType.TEMPERATURE);
        assertThat(statsQueryResponseDTO.metricResponses.get(0).getMetricResponseDTOByType(statsQueryResponseDTO.metricResponses, MetricType.TEMPERATURE).metricValue).isEqualTo(32.0);
    }

    @Test
    void queryMetricsSuccessAverageStatsTwo(){
        final StatsQueryRequestDTO statsQueryRequestDTO = testUtils.generateStatsQueryRequestDTO(Arrays.asList(1L), StatType.AVG, false, false, true, true, false, "2023-05-01", "2023-06-10");

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<StatsQueryRequestDTO> requestEntity = new HttpEntity<>(statsQueryRequestDTO, headers);

        final ResponseEntity<StatsQueryResponseDTO> response = restTemplate.postForEntity(
                "/v1/weather/stats",
                requestEntity,
                StatsQueryResponseDTO.class
        );

        final StatsQueryResponseDTO statsQueryResponseDTO = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(statsQueryResponseDTO.metricResponses.size()).isEqualTo(2);
        assertThat(statsQueryResponseDTO.metricResponses.get(0).getMetricResponseDTOByType(statsQueryResponseDTO.metricResponses, MetricType.RAINFALL).metric).isEqualTo(MetricType.RAINFALL);
        assertThat(statsQueryResponseDTO.metricResponses.get(0).getMetricResponseDTOByType(statsQueryResponseDTO.metricResponses, MetricType.RAINFALL).metricValue).isEqualTo(42.0);
        assertThat(statsQueryResponseDTO.metricResponses.get(0).getMetricResponseDTOByType(statsQueryResponseDTO.metricResponses, MetricType.WINDSPEED).metric).isEqualTo(MetricType.WINDSPEED);
        assertThat(statsQueryResponseDTO.metricResponses.get(0).getMetricResponseDTOByType(statsQueryResponseDTO.metricResponses, MetricType.WINDSPEED).metricValue).isEqualTo(37.0);
    }


    @Test
    void queryMetricsSuccessMinimumStatsOne(){
        final StatsQueryRequestDTO statsQueryRequestDTO = testUtils.generateStatsQueryRequestDTO(Arrays.asList(2L,3L), StatType.MIN, false, true, false, true, true, "2023-05-01", "2023-06-03");

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<StatsQueryRequestDTO> requestEntity = new HttpEntity<>(statsQueryRequestDTO, headers);

        final ResponseEntity<StatsQueryResponseDTO> response = restTemplate.postForEntity(
                "/v1/weather/stats",
                requestEntity,
                StatsQueryResponseDTO.class
        );

        final StatsQueryResponseDTO statsQueryResponseDTO = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(statsQueryResponseDTO.metricResponses.size()).isEqualTo(3);
        assertThat(statsQueryResponseDTO.metricResponses.get(0).getMetricResponseDTOByType(statsQueryResponseDTO.metricResponses, MetricType.HUMIDITY).metric).isEqualTo(MetricType.HUMIDITY);
        assertThat(statsQueryResponseDTO.metricResponses.get(0).getMetricResponseDTOByType(statsQueryResponseDTO.metricResponses, MetricType.HUMIDITY).metricValue).isEqualTo(52.0);
        assertThat(statsQueryResponseDTO.metricResponses.get(0).getMetricResponseDTOByType(statsQueryResponseDTO.metricResponses, MetricType.WINDSPEED).metric).isEqualTo(MetricType.WINDSPEED);
        assertThat(statsQueryResponseDTO.metricResponses.get(0).getMetricResponseDTOByType(statsQueryResponseDTO.metricResponses, MetricType.WINDSPEED).metricValue).isEqualTo(12.0);
        assertThat(statsQueryResponseDTO.metricResponses.get(0).getMetricResponseDTOByType(statsQueryResponseDTO.metricResponses, MetricType.TEMPERATURE).metric).isEqualTo(MetricType.TEMPERATURE);
        assertThat(statsQueryResponseDTO.metricResponses.get(0).getMetricResponseDTOByType(statsQueryResponseDTO.metricResponses, MetricType.TEMPERATURE).metricValue).isEqualTo(22.0);
    }

    @Test
    void queryMetricsSuccessMinimumStatsTwo(){
        final StatsQueryRequestDTO statsQueryRequestDTO = testUtils.generateStatsQueryRequestDTO(null, StatType.MIN, true, true, false, false, true, null, null);

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<StatsQueryRequestDTO> requestEntity = new HttpEntity<>(statsQueryRequestDTO, headers);

        final ResponseEntity<StatsQueryResponseDTO> response = restTemplate.postForEntity(
                "/v1/weather/stats",
                requestEntity,
                StatsQueryResponseDTO.class
        );

        final StatsQueryResponseDTO statsQueryResponseDTO = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(statsQueryResponseDTO.metricResponses.size()).isEqualTo(2);
        assertThat(statsQueryResponseDTO.metricResponses.get(0).getMetricResponseDTOByType(statsQueryResponseDTO.metricResponses, MetricType.HUMIDITY).metric).isEqualTo(MetricType.HUMIDITY);
        assertThat(statsQueryResponseDTO.metricResponses.get(0).getMetricResponseDTOByType(statsQueryResponseDTO.metricResponses, MetricType.HUMIDITY).metricValue).isEqualTo(20.0);
        assertThat(statsQueryResponseDTO.metricResponses.get(0).getMetricResponseDTOByType(statsQueryResponseDTO.metricResponses, MetricType.TEMPERATURE).metric).isEqualTo(MetricType.TEMPERATURE);
        assertThat(statsQueryResponseDTO.metricResponses.get(0).getMetricResponseDTOByType(statsQueryResponseDTO.metricResponses, MetricType.TEMPERATURE).metricValue).isEqualTo(18.0);
    }

    @Test
    void queryMetricsSuccessMaximumStatsOne(){
        final StatsQueryRequestDTO statsQueryRequestDTO = testUtils.generateStatsQueryRequestDTO(Arrays.asList(1L,3L, 4L), StatType.MAX, false, true, true, true, true, "2023-05-10", "2023-06-07");

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<StatsQueryRequestDTO> requestEntity = new HttpEntity<>(statsQueryRequestDTO, headers);

        final ResponseEntity<StatsQueryResponseDTO> response = restTemplate.postForEntity(
                "/v1/weather/stats",
                requestEntity,
                StatsQueryResponseDTO.class
        );

        final StatsQueryResponseDTO statsQueryResponseDTO = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(statsQueryResponseDTO.metricResponses.size()).isEqualTo(4);
        assertThat(statsQueryResponseDTO.metricResponses.get(0).getMetricResponseDTOByType(statsQueryResponseDTO.metricResponses, MetricType.HUMIDITY).metric).isEqualTo(MetricType.HUMIDITY);
        assertThat(statsQueryResponseDTO.metricResponses.get(0).getMetricResponseDTOByType(statsQueryResponseDTO.metricResponses, MetricType.HUMIDITY).metricValue).isEqualTo(78.0);
        assertThat(statsQueryResponseDTO.metricResponses.get(0).getMetricResponseDTOByType(statsQueryResponseDTO.metricResponses, MetricType.WINDSPEED).metric).isEqualTo(MetricType.WINDSPEED);
        assertThat(statsQueryResponseDTO.metricResponses.get(0).getMetricResponseDTOByType(statsQueryResponseDTO.metricResponses, MetricType.WINDSPEED).metricValue).isEqualTo(38.0);
        assertThat(statsQueryResponseDTO.metricResponses.get(0).getMetricResponseDTOByType(statsQueryResponseDTO.metricResponses, MetricType.TEMPERATURE).metric).isEqualTo(MetricType.TEMPERATURE);
        assertThat(statsQueryResponseDTO.metricResponses.get(0).getMetricResponseDTOByType(statsQueryResponseDTO.metricResponses, MetricType.TEMPERATURE).metricValue).isEqualTo(48.0);
        assertThat(statsQueryResponseDTO.metricResponses.get(0).getMetricResponseDTOByType(statsQueryResponseDTO.metricResponses, MetricType.RAINFALL).metric).isEqualTo(MetricType.RAINFALL);
        assertThat(statsQueryResponseDTO.metricResponses.get(0).getMetricResponseDTOByType(statsQueryResponseDTO.metricResponses, MetricType.RAINFALL).metricValue).isEqualTo(43.0);
    }

    @Test
    void updateSensorDataReading200Response(){
        final SensorDataDTO request = testUtils.generateSensorDataDTOForUpdate(1000L, 1L);

        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        final HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        final HttpEntity<SensorDataDTO> requestEntity = new HttpEntity<>(request, headers);

        final ResponseEntity<SensorDataDTO> updateResponse = restTemplate.exchange(
                "/v1/weather/metrics",
                HttpMethod.PATCH,
                requestEntity,
                SensorDataDTO.class
        );

        final SensorDataDTO sensorDateDTO = updateResponse.getBody();
        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(sensorDateDTO.metrics.size()).isEqualTo(4);
        assertThat(sensorDateDTO.sampleId).isEqualTo(1000);
        assertThat(sensorDateDTO.sensorId).isEqualTo(1);
        assertThat(sensorDateDTO.metrics.get(0).getMetricByName(sensorDateDTO.metrics, MetricType.TEMPERATURE).metricType).isEqualTo(MetricType.TEMPERATURE);
        assertThat(sensorDateDTO.metrics.get(0).getMetricByName(sensorDateDTO.metrics, MetricType.TEMPERATURE).metricValue).isEqualTo(18);
        assertThat(sensorDateDTO.metrics.get(0).getMetricByName(sensorDateDTO.metrics, MetricType.HUMIDITY).metricType).isEqualTo(MetricType.HUMIDITY);
        assertThat(sensorDateDTO.metrics.get(0).getMetricByName(sensorDateDTO.metrics, MetricType.HUMIDITY).metricValue).isEqualTo(20);
        assertThat(sensorDateDTO.metrics.get(0).getMetricByName(sensorDateDTO.metrics, MetricType.RAINFALL).metricType).isEqualTo(MetricType.RAINFALL);
        assertThat(sensorDateDTO.metrics.get(0).getMetricByName(sensorDateDTO.metrics, MetricType.RAINFALL).metricValue).isEqualTo(6);
        assertThat(sensorDateDTO.metrics.get(0).getMetricByName(sensorDateDTO.metrics, MetricType.WINDSPEED).metricType).isEqualTo(MetricType.WINDSPEED);
        assertThat(sensorDateDTO.metrics.get(0).getMetricByName(sensorDateDTO.metrics, MetricType.WINDSPEED).metricValue).isNull();
    }

    @Test
    void updateSensorDataReading404(){
        final SensorDataDTO request = testUtils.generateSensorDataDTOForUpdate(10000L, 1L);

        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        final HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        final HttpEntity<SensorDataDTO> requestEntity = new HttpEntity<>(request, headers);

        final ResponseEntity<SensorDataDTO> updateResponse = restTemplate.exchange(
                "/v1/weather/metrics",
                HttpMethod.PATCH,
                requestEntity,
                SensorDataDTO.class
        );
        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
     }

    private ResponseEntity<SensorDataDTO> createSensorData(final Long sampleId, final Long sensorId) {
        final SensorDataDTO request = testUtils.generateSensorDataDTO(sampleId, sensorId);
        return restTemplate.postForEntity("/v1/weather/metrics", request, SensorDataDTO.class);
    }
}
