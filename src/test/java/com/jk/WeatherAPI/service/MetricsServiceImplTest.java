package com.jk.WeatherAPI.service;

import com.jk.WeatherAPI.dto.SensorDataDTO;
import com.jk.WeatherAPI.dto.enums.MetricType;
import com.jk.WeatherAPI.entities.SensorData;
import com.jk.WeatherAPI.exception.AppException;
import com.jk.WeatherAPI.repository.SensorDataRepo;
import com.jk.WeatherAPI.util.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
public class MetricsServiceImplTest {

    @Mock
    private SensorDataRepo sensorDateRepository;

    @InjectMocks
    private MetricsServiceImpl metricsService;

    final TestUtils testUtils = new TestUtils();


    @Test
    void getAllSamplesNoSamplesInDB() {
        given(sensorDateRepository.findAll()).willReturn(new ArrayList<>());

        // Invoke the method under test
        List<SensorDataDTO> samples = metricsService.getAllSamples();
        assertThat(samples.size()).isEqualTo(0);
    }


    @Test
    void getAllSamplesSuccess() {
        List<SensorData> sensorData = new ArrayList<>();
        sensorData.add(testUtils.generateSensorData(1L, 11L, null, 10.0, 20.0, 30.0, 40.0));
        sensorData.add(testUtils.generateSensorData(2L, 22L, null, 101.0, 202.0, 303.0, 404.0));

        given(sensorDateRepository.findAll()).willReturn(sensorData);

        // Invoke the method under test
        List<SensorDataDTO> samples = metricsService.getAllSamples();
        assertThat(samples.size()).isEqualTo(2);
        assertThat(samples.get(0).sampleId).isEqualTo(1);
        assertThat(samples.get(0).sensorId).isEqualTo(11);
        assertThat(samples.get(0).metrics.size()).isEqualTo(4);
        assertThat(samples.get(0).metrics.get(0).getMetricByName(samples.get(0).metrics, MetricType.TEMPERATURE).metricType).isEqualTo(MetricType.TEMPERATURE);
        assertThat(samples.get(0).metrics.get(0).getMetricByName(samples.get(0).metrics, MetricType.TEMPERATURE).metricValue).isEqualTo(10.0);
        assertThat(samples.get(0).metrics.get(0).getMetricByName(samples.get(0).metrics, MetricType.RAINFALL).metricType).isEqualTo(MetricType.RAINFALL);
        assertThat(samples.get(0).metrics.get(0).getMetricByName(samples.get(0).metrics, MetricType.RAINFALL).metricValue).isEqualTo(20.0);
        assertThat(samples.get(0).metrics.get(0).getMetricByName(samples.get(0).metrics, MetricType.WINDSPEED).metricType).isEqualTo(MetricType.WINDSPEED);
        assertThat(samples.get(0).metrics.get(0).getMetricByName(samples.get(0).metrics, MetricType.WINDSPEED).metricValue).isEqualTo(30.0);
        assertThat(samples.get(0).metrics.get(0).getMetricByName(samples.get(0).metrics, MetricType.HUMIDITY).metricType).isEqualTo(MetricType.HUMIDITY);
        assertThat(samples.get(0).metrics.get(0).getMetricByName(samples.get(0).metrics, MetricType.HUMIDITY).metricValue).isEqualTo(40.0);
        assertThat(samples.get(1).sampleId).isEqualTo(2);
        assertThat(samples.get(1).sensorId).isEqualTo(22);
        assertThat(samples.get(1).metrics.size()).isEqualTo(4);
        assertThat(samples.get(1).metrics.get(1).getMetricByName(samples.get(1).metrics, MetricType.TEMPERATURE).metricType).isEqualTo(MetricType.TEMPERATURE);
        assertThat(samples.get(1).metrics.get(1).getMetricByName(samples.get(1).metrics, MetricType.TEMPERATURE).metricValue).isEqualTo(101.0);
        assertThat(samples.get(1).metrics.get(1).getMetricByName(samples.get(1).metrics, MetricType.RAINFALL).metricType).isEqualTo(MetricType.RAINFALL);
        assertThat(samples.get(1).metrics.get(1).getMetricByName(samples.get(1).metrics, MetricType.RAINFALL).metricValue).isEqualTo(202.0);
        assertThat(samples.get(1).metrics.get(1).getMetricByName(samples.get(1).metrics, MetricType.WINDSPEED).metricType).isEqualTo(MetricType.WINDSPEED);
        assertThat(samples.get(1).metrics.get(1).getMetricByName(samples.get(1).metrics, MetricType.WINDSPEED).metricValue).isEqualTo(303.0);
        assertThat(samples.get(1).metrics.get(1).getMetricByName(samples.get(1).metrics, MetricType.HUMIDITY).metricType).isEqualTo(MetricType.HUMIDITY);
        assertThat(samples.get(1).metrics.get(1).getMetricByName(samples.get(1).metrics, MetricType.HUMIDITY).metricValue).isEqualTo(404.0);
    }

    @Test
    void getSampleByIdNotFound() {
        given(sensorDateRepository.findBySampleId(123L)).willReturn(Optional.empty());

        AppException exception = Assertions.assertThrows(AppException.class, () -> {
            metricsService.getSampleBySampleId(123L);
        });

        assertThat(exception.getErrorMessage()).isEqualTo("Cannot find Sample with sample Id " + 123);
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getSampleByIdSuccess() {
        given(sensorDateRepository.findBySampleId(123L)).willReturn(Optional.of(testUtils.generateSensorData(1L, 11L, null, 10.0, 20.0, 30.0, 40.0)));

        SensorDataDTO sensorDataDTO = metricsService.getSampleBySampleId(123L);
        assertThat(sensorDataDTO.sampleId).isEqualTo(1);
        assertThat(sensorDataDTO.sensorId).isEqualTo(11);
        assertThat(sensorDataDTO.metrics.size()).isEqualTo(4);
        assertThat(sensorDataDTO.metrics.get(0).getMetricByName(sensorDataDTO.metrics, MetricType.TEMPERATURE).metricType).isEqualTo(MetricType.TEMPERATURE);
        assertThat(sensorDataDTO.metrics.get(0).getMetricByName(sensorDataDTO.metrics, MetricType.TEMPERATURE).metricValue).isEqualTo(10.0);
        assertThat(sensorDataDTO.metrics.get(0).getMetricByName(sensorDataDTO.metrics, MetricType.RAINFALL).metricType).isEqualTo(MetricType.RAINFALL);
        assertThat(sensorDataDTO.metrics.get(0).getMetricByName(sensorDataDTO.metrics, MetricType.RAINFALL).metricValue).isEqualTo(20.0);
        assertThat(sensorDataDTO.metrics.get(0).getMetricByName(sensorDataDTO.metrics, MetricType.WINDSPEED).metricType).isEqualTo(MetricType.WINDSPEED);
        assertThat(sensorDataDTO.metrics.get(0).getMetricByName(sensorDataDTO.metrics, MetricType.WINDSPEED).metricValue).isEqualTo(30.0);
        assertThat(sensorDataDTO.metrics.get(0).getMetricByName(sensorDataDTO.metrics, MetricType.HUMIDITY).metricType).isEqualTo(MetricType.HUMIDITY);
        assertThat(sensorDataDTO.metrics.get(0).getMetricByName(sensorDataDTO.metrics, MetricType.HUMIDITY).metricValue).isEqualTo(40.0);
    }


    @Test
    void createSampleSuccess() {
        final SensorData persistedSensorData = testUtils.generateSensorData(1L, 11L, null, 10.0, 0.0, 0.0, 20.0);

        given(sensorDateRepository.save(any())).willReturn(persistedSensorData);
        given(sensorDateRepository.findBySampleId(1L)).willReturn(Optional.empty());

        final SensorDataDTO sensorDataDTO = metricsService.createSample(testUtils.generateSensorDataDTO(1L, 11L));
        assertThat(sensorDataDTO.sampleId).isEqualTo(1);
        assertThat(sensorDataDTO.sensorId).isEqualTo(11);
        assertThat(sensorDataDTO.metrics.size()).isEqualTo(4);
        assertThat(sensorDataDTO.metrics.get(0).getMetricByName(sensorDataDTO.metrics, MetricType.TEMPERATURE).metricType).isEqualTo(MetricType.TEMPERATURE);
        assertThat(sensorDataDTO.metrics.get(0).getMetricByName(sensorDataDTO.metrics, MetricType.TEMPERATURE).metricValue).isEqualTo(10.0);
        assertThat(sensorDataDTO.metrics.get(0).getMetricByName(sensorDataDTO.metrics, MetricType.RAINFALL).metricType).isEqualTo(MetricType.RAINFALL);
        assertThat(sensorDataDTO.metrics.get(0).getMetricByName(sensorDataDTO.metrics, MetricType.RAINFALL).metricValue).isEqualTo(0.0);
        assertThat(sensorDataDTO.metrics.get(0).getMetricByName(sensorDataDTO.metrics, MetricType.WINDSPEED).metricType).isEqualTo(MetricType.WINDSPEED);
        assertThat(sensorDataDTO.metrics.get(0).getMetricByName(sensorDataDTO.metrics, MetricType.WINDSPEED).metricValue).isEqualTo(0.0);
        assertThat(sensorDataDTO.metrics.get(0).getMetricByName(sensorDataDTO.metrics, MetricType.HUMIDITY).metricType).isEqualTo(MetricType.HUMIDITY);
        assertThat(sensorDataDTO.metrics.get(0).getMetricByName(sensorDataDTO.metrics, MetricType.HUMIDITY).metricValue).isEqualTo(20.0);
    }

    @Test
    void createSampleAlreadyFound() {
        final SensorData existingSensorData = testUtils.generateSensorData(1L, 11L, null, 10.0, 0.0, 0.0, 20.0);
        given(sensorDateRepository.findBySampleId(1L)).willReturn(Optional.of(existingSensorData));

        AppException exception = Assertions.assertThrows(AppException.class, () -> {
            metricsService.createSample(testUtils.generateSensorDataDTO(1L, 11L));
        });

        assertThat(exception.getErrorMessage()).isEqualTo("A sample with sampleId 1 already exists");
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void createSampleInvalidInputMetrics() {
        AppException exception = Assertions.assertThrows(AppException.class, () -> {
            metricsService.createSample(testUtils.generateInvalidSensorDataDTO(1L, 11L));
        });

        assertThat(exception.getErrorMessage()).isEqualTo("Bad Request");
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }


    @Test
    void updateSampleSuccess() {
        final SensorData existingSensorData = testUtils.generateSensorData(1L, 11L, null, 10.0, 0.0, 0.0, 20.0);
        given(sensorDateRepository.findBySampleId(1L)).willReturn(Optional.of(existingSensorData));
        given(sensorDateRepository.save(any())).willReturn(testUtils.generateSensorData(1L, 111L, null, 10.0, 0.0, 0.0, 20.0));

        final SensorDataDTO sensorDataDTO = metricsService.updateSample(testUtils.generateSensorDataDTO(1L, 111L));
        assertThat(sensorDataDTO.sampleId).isEqualTo(1);
        assertThat(sensorDataDTO.sensorId).isEqualTo(111);
        assertThat(sensorDataDTO.metrics.size()).isEqualTo(4);
        assertThat(sensorDataDTO.metrics.get(0).getMetricByName(sensorDataDTO.metrics, MetricType.TEMPERATURE).metricType).isEqualTo(MetricType.TEMPERATURE);
        assertThat(sensorDataDTO.metrics.get(0).getMetricByName(sensorDataDTO.metrics, MetricType.TEMPERATURE).metricValue).isEqualTo(10.0);
        assertThat(sensorDataDTO.metrics.get(0).getMetricByName(sensorDataDTO.metrics, MetricType.RAINFALL).metricType).isEqualTo(MetricType.RAINFALL);
        assertThat(sensorDataDTO.metrics.get(0).getMetricByName(sensorDataDTO.metrics, MetricType.RAINFALL).metricValue).isEqualTo(0.0);
        assertThat(sensorDataDTO.metrics.get(0).getMetricByName(sensorDataDTO.metrics, MetricType.WINDSPEED).metricType).isEqualTo(MetricType.WINDSPEED);
        assertThat(sensorDataDTO.metrics.get(0).getMetricByName(sensorDataDTO.metrics, MetricType.WINDSPEED).metricValue).isEqualTo(0.0);
        assertThat(sensorDataDTO.metrics.get(0).getMetricByName(sensorDataDTO.metrics, MetricType.HUMIDITY).metricType).isEqualTo(MetricType.HUMIDITY);
        assertThat(sensorDataDTO.metrics.get(0).getMetricByName(sensorDataDTO.metrics, MetricType.HUMIDITY).metricValue).isEqualTo(20.0);
    }

    @Test
    void updateSampleNotFound() {
        given(sensorDateRepository.findBySampleId(1L)).willReturn(Optional.empty());

        AppException exception = Assertions.assertThrows(AppException.class, () -> {
            metricsService.updateSample(testUtils.generateSensorDataDTO(1L, 11L));
        });

        assertThat(exception.getErrorMessage()).isEqualTo("A sample with sample id 1 does not exist. Cannot update");
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void upateSampleInvalidInputMetrics() {
        AppException exception = Assertions.assertThrows(AppException.class, () -> {
            metricsService.updateSample(testUtils.generateInvalidSensorDataDTO(1L, 11L));
        });

        assertThat(exception.getErrorMessage()).isEqualTo("Bad Request");
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void deleteSampleNotFound() {
        given(sensorDateRepository.findBySampleId(1L)).willReturn(Optional.empty());

        AppException exception = Assertions.assertThrows(AppException.class, () -> {
            metricsService.deleteSample(1L);
        });

        assertThat(exception.getErrorMessage()).isEqualTo("Cannot find Sample with sample Id 1");
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteSampleSuccess() {
        final SensorData existingSensorData = testUtils.generateSensorData(1L, 11L, null, 10.0, 0.0, 0.0, 20.0);
        given(sensorDateRepository.findBySampleId(1L)).willReturn(Optional.of(existingSensorData));
        boolean response = metricsService.deleteSample(1L);
        assertThat(response).isTrue();
    }
}
