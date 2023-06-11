package com.jk.WeatherAPI.service;

import com.jk.WeatherAPI.dto.SensorDataDTO;
import com.jk.WeatherAPI.dto.StatsQueryResponseDTO;
import com.jk.WeatherAPI.dto.enums.MetricType;
import com.jk.WeatherAPI.dto.enums.StatType;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MetricsServiceImplTest {

    @Mock
    private SensorDataRepo sensorDateRepository;

    @InjectMocks
    private MetricsServiceImpl metricsService;

    final TestUtils testUtils = new TestUtils();

    @Test
    void queryMetricsAllMetricTypesAverageSuccess() {
        given(sensorDateRepository.getAverageHumidityBySensorIdsAndDateRange(any(), any(), any())).willReturn(10.0);
        given(sensorDateRepository.getAverageWindspeedBySensorIdsAndDateRange(any(), any(), any())).willReturn(20.0);
        given(sensorDateRepository.getAverageTemperatureBySensorIdsAndDateRange(any(), any(), any())).willReturn(30.0);
        given(sensorDateRepository.getAverageRainfallBySensorIdsAndDateRange(any(), any(), any())).willReturn(40.0);

        final StatsQueryResponseDTO response =metricsService.getStats(testUtils.generateStatsQueryRequestDTO(Arrays.asList(1L, 2L), Arrays.asList(MetricType.HUMIDITY, MetricType.RAINFALL, MetricType.TEMPERATURE, MetricType.WINDSPEED), StatType.AVG, "2023-06-01", "2023-06-07", false));
        verify(sensorDateRepository, times(1)).getAverageHumidityBySensorIdsAndDateRange(any(), any(), any());
        verify(sensorDateRepository, times(1)).getAverageWindspeedBySensorIdsAndDateRange(any(), any(), any());
        verify(sensorDateRepository, times(1)).getAverageTemperatureBySensorIdsAndDateRange(any(), any(), any());
        verify(sensorDateRepository, times(1)).getAverageRainfallBySensorIdsAndDateRange(any(), any(), any());

        assertThat(response.metricResponses.size()).isEqualTo(4);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.RAINFALL).metric).isEqualTo(MetricType.RAINFALL);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.RAINFALL).statType).isEqualTo(StatType.AVG);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.RAINFALL).metricValue).isEqualTo(40.0);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.WINDSPEED).metric).isEqualTo(MetricType.WINDSPEED);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.WINDSPEED).statType).isEqualTo(StatType.AVG);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.WINDSPEED).metricValue).isEqualTo(20.0);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.HUMIDITY).metric).isEqualTo(MetricType.HUMIDITY);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.HUMIDITY).statType).isEqualTo(StatType.AVG);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.HUMIDITY).metricValue).isEqualTo(10.0);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.TEMPERATURE).metric).isEqualTo(MetricType.TEMPERATURE);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.TEMPERATURE).statType).isEqualTo(StatType.AVG);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.TEMPERATURE).metricValue).isEqualTo(30.0);

    }

    @Test
    void queryMetricsAllMetricTypesMinimumSuccess() {
        given(sensorDateRepository.getMinHumidityBySensorIdsAndDateRange(any(), any(), any())).willReturn(10.0);
        given(sensorDateRepository.getMinWindspeedBySensorIdsAndDateRange(any(), any(), any())).willReturn(20.0);
        given(sensorDateRepository.getMinTemperatureBySensorIdsAndDateRange(any(), any(), any())).willReturn(30.0);
        given(sensorDateRepository.getMinRainfallBySensorIdsAndDateRange(any(), any(), any())).willReturn(40.0);

        final StatsQueryResponseDTO response =metricsService.getStats(testUtils.generateStatsQueryRequestDTO(Arrays.asList(1L, 2L), Arrays.asList(MetricType.HUMIDITY, MetricType.RAINFALL, MetricType.TEMPERATURE, MetricType.WINDSPEED), StatType.MIN, "2023-06-01", "2023-06-07", false));
        verify(sensorDateRepository, times(1)).getMinHumidityBySensorIdsAndDateRange(any(), any(), any());
        verify(sensorDateRepository, times(1)).getMinWindspeedBySensorIdsAndDateRange(any(), any(), any());
        verify(sensorDateRepository, times(1)).getMinTemperatureBySensorIdsAndDateRange(any(), any(), any());
        verify(sensorDateRepository, times(1)).getMinRainfallBySensorIdsAndDateRange(any(), any(), any());

        assertThat(response.metricResponses.size()).isEqualTo(4);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.RAINFALL).metric).isEqualTo(MetricType.RAINFALL);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.RAINFALL).statType).isEqualTo(StatType.MIN);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.RAINFALL).metricValue).isEqualTo(40.0);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.WINDSPEED).metric).isEqualTo(MetricType.WINDSPEED);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.WINDSPEED).statType).isEqualTo(StatType.MIN);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.WINDSPEED).metricValue).isEqualTo(20.0);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.HUMIDITY).metric).isEqualTo(MetricType.HUMIDITY);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.HUMIDITY).statType).isEqualTo(StatType.MIN);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.HUMIDITY).metricValue).isEqualTo(10.0);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.TEMPERATURE).metric).isEqualTo(MetricType.TEMPERATURE);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.TEMPERATURE).statType).isEqualTo(StatType.MIN);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.TEMPERATURE).metricValue).isEqualTo(30.0);

    }

    @Test
    void queryMetricsAllMetricTypesMaximumSuccess() {
        given(sensorDateRepository.getMaxHumidityBySensorIdsAndDateRange(any(), any(), any())).willReturn(10.0);
        given(sensorDateRepository.getMaxWindspeedBySensorIdsAndDateRange(any(), any(), any())).willReturn(20.0);
        given(sensorDateRepository.getMaxTemperatureBySensorIdsAndDateRange(any(), any(), any())).willReturn(30.0);
        given(sensorDateRepository.getMaxRainfallBySensorIdsAndDateRange(any(), any(), any())).willReturn(40.0);

        final StatsQueryResponseDTO response = metricsService.getStats(testUtils.generateStatsQueryRequestDTO(Arrays.asList(1L, 2L), Arrays.asList(MetricType.HUMIDITY, MetricType.RAINFALL, MetricType.TEMPERATURE, MetricType.WINDSPEED), StatType.MAX, "2023-06-01", "2023-06-07", false));
        verify(sensorDateRepository, times(1)).getMaxHumidityBySensorIdsAndDateRange(any(), any(), any());
        verify(sensorDateRepository, times(1)).getMaxWindspeedBySensorIdsAndDateRange(any(), any(), any());
        verify(sensorDateRepository, times(1)).getMaxTemperatureBySensorIdsAndDateRange(any(), any(), any());
        verify(sensorDateRepository, times(1)).getMaxRainfallBySensorIdsAndDateRange(any(), any(), any());

        assertThat(response.metricResponses.size()).isEqualTo(4);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.RAINFALL).metric).isEqualTo(MetricType.RAINFALL);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.RAINFALL).statType).isEqualTo(StatType.MAX);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.RAINFALL).metricValue).isEqualTo(40.0);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.WINDSPEED).metric).isEqualTo(MetricType.WINDSPEED);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.WINDSPEED).statType).isEqualTo(StatType.MAX);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.WINDSPEED).metricValue).isEqualTo(20.0);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.HUMIDITY).metric).isEqualTo(MetricType.HUMIDITY);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.HUMIDITY).statType).isEqualTo(StatType.MAX);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.HUMIDITY).metricValue).isEqualTo(10.0);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.TEMPERATURE).metric).isEqualTo(MetricType.TEMPERATURE);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.TEMPERATURE).statType).isEqualTo(StatType.MAX);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.TEMPERATURE).metricValue).isEqualTo(30.0);

    }

    @Test
    void queryMetricsAllMetricTypesSumSuccess() {
        given(sensorDateRepository.getSumOfHumidityBySensorIdsAndDateRange(any(), any(), any())).willReturn(10.0);
        given(sensorDateRepository.getSumOfWindspeedBySensorIdsAndDateRange(any(), any(), any())).willReturn(20.0);
        given(sensorDateRepository.getSumOfTemperatureBySensorIdsAndDateRange(any(), any(), any())).willReturn(30.0);
        given(sensorDateRepository.getSumOfRainfallBySensorIdsAndDateRange(any(), any(), any())).willReturn(40.0);

        final StatsQueryResponseDTO response = metricsService.getStats(testUtils.generateStatsQueryRequestDTO(Arrays.asList(1L, 2L), Arrays.asList(MetricType.HUMIDITY, MetricType.RAINFALL, MetricType.TEMPERATURE, MetricType.WINDSPEED), StatType.SUM, "2023-06-01", "2023-06-07", false));
        verify(sensorDateRepository, times(1)).getSumOfHumidityBySensorIdsAndDateRange(any(), any(), any());
        verify(sensorDateRepository, times(1)).getSumOfWindspeedBySensorIdsAndDateRange(any(), any(), any());
        verify(sensorDateRepository, times(1)).getSumOfTemperatureBySensorIdsAndDateRange(any(), any(), any());
        verify(sensorDateRepository, times(1)).getSumOfRainfallBySensorIdsAndDateRange(any(), any(), any());

        assertThat(response.metricResponses.size()).isEqualTo(4);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.RAINFALL).metric).isEqualTo(MetricType.RAINFALL);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.RAINFALL).statType).isEqualTo(StatType.SUM);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.RAINFALL).metricValue).isEqualTo(40.0);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.WINDSPEED).metric).isEqualTo(MetricType.WINDSPEED);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.WINDSPEED).statType).isEqualTo(StatType.SUM);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.WINDSPEED).metricValue).isEqualTo(20.0);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.HUMIDITY).metric).isEqualTo(MetricType.HUMIDITY);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.HUMIDITY).statType).isEqualTo(StatType.SUM);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.HUMIDITY).metricValue).isEqualTo(10.0);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.TEMPERATURE).metric).isEqualTo(MetricType.TEMPERATURE);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.TEMPERATURE).statType).isEqualTo(StatType.SUM);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.TEMPERATURE).metricValue).isEqualTo(30.0);
    }

    @Test
    void queryMetricsAllMetricTypesAverageAllSensorsSuccess() {
        given(sensorDateRepository.getAverageTemperatureForDateRangeAllSensors(any(), any())).willReturn(10.0);
        given(sensorDateRepository.getAverageWindspeedForDateRangeAllSensors(any(), any())).willReturn(20.0);
        given(sensorDateRepository.getAverageHumidityForDateRangeAllSensors(any(), any())).willReturn(30.0);
        given(sensorDateRepository.getAverageRainfallForDateRangeAllSensors(any(), any())).willReturn(40.0);

        final StatsQueryResponseDTO response = metricsService.getStats(testUtils.generateStatsQueryRequestDTO(Arrays.asList(1L, 2L), Arrays.asList(MetricType.HUMIDITY, MetricType.RAINFALL, MetricType.TEMPERATURE, MetricType.WINDSPEED), StatType.AVG, "2023-06-01", "2023-06-07", true));
        verify(sensorDateRepository, times(1)).getAverageTemperatureForDateRangeAllSensors(any(), any());
        verify(sensorDateRepository, times(1)).getAverageWindspeedForDateRangeAllSensors(any(), any());
        verify(sensorDateRepository, times(1)).getAverageHumidityForDateRangeAllSensors(any(), any());
        verify(sensorDateRepository, times(1)).getAverageRainfallForDateRangeAllSensors(any(), any());

        assertThat(response.metricResponses.size()).isEqualTo(4);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.RAINFALL).metric).isEqualTo(MetricType.RAINFALL);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.RAINFALL).statType).isEqualTo(StatType.AVG);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.RAINFALL).metricValue).isEqualTo(40.0);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.WINDSPEED).metric).isEqualTo(MetricType.WINDSPEED);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.WINDSPEED).statType).isEqualTo(StatType.AVG);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.WINDSPEED).metricValue).isEqualTo(20.0);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.HUMIDITY).metric).isEqualTo(MetricType.HUMIDITY);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.HUMIDITY).statType).isEqualTo(StatType.AVG);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.HUMIDITY).metricValue).isEqualTo(30.0);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.TEMPERATURE).metric).isEqualTo(MetricType.TEMPERATURE);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.TEMPERATURE).statType).isEqualTo(StatType.AVG);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.TEMPERATURE).metricValue).isEqualTo(10.0);

    }

    @Test
    void queryMetricsAllMetricTypesMinimumAllSensorsSuccess() {
        given(sensorDateRepository.getMinTemperatureForDateRangeAllSensors(any(), any())).willReturn(10.0);
        given(sensorDateRepository.getMinWindspeedForDateRangeAllSensors(any(), any())).willReturn(20.0);
        given(sensorDateRepository.getMinHumidityForDateRangeAllSensors(any(), any())).willReturn(30.0);
        given(sensorDateRepository.getMinRainfallForDateRangeAllSensors(any(), any())).willReturn(40.0);

        final StatsQueryResponseDTO response = metricsService.getStats(testUtils.generateStatsQueryRequestDTO(Arrays.asList(1L, 2L), Arrays.asList(MetricType.HUMIDITY, MetricType.RAINFALL, MetricType.TEMPERATURE, MetricType.WINDSPEED), StatType.MIN, "2023-06-01", "2023-06-07", true));
        verify(sensorDateRepository, times(1)).getMinTemperatureForDateRangeAllSensors(any(), any());
        verify(sensorDateRepository, times(1)).getMinWindspeedForDateRangeAllSensors(any(), any());
        verify(sensorDateRepository, times(1)).getMinHumidityForDateRangeAllSensors(any(), any());
        verify(sensorDateRepository, times(1)).getMinRainfallForDateRangeAllSensors(any(), any());

        assertThat(response.metricResponses.size()).isEqualTo(4);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.RAINFALL).metric).isEqualTo(MetricType.RAINFALL);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.RAINFALL).statType).isEqualTo(StatType.MIN);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.RAINFALL).metricValue).isEqualTo(40.0);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.WINDSPEED).metric).isEqualTo(MetricType.WINDSPEED);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.WINDSPEED).statType).isEqualTo(StatType.MIN);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.WINDSPEED).metricValue).isEqualTo(20.0);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.HUMIDITY).metric).isEqualTo(MetricType.HUMIDITY);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.HUMIDITY).statType).isEqualTo(StatType.MIN);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.HUMIDITY).metricValue).isEqualTo(30.0);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.TEMPERATURE).metric).isEqualTo(MetricType.TEMPERATURE);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.TEMPERATURE).statType).isEqualTo(StatType.MIN);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.TEMPERATURE).metricValue).isEqualTo(10.0);

    }

    @Test
    void queryMetricsAllMetricTypesMaximumAllSensorsSuccess() {
        given(sensorDateRepository.getMaxTemperatureForDateRangeAllSensors(any(), any())).willReturn(10.0);
        given(sensorDateRepository.getMaxWindspeedForDateRangeAllSensors(any(), any())).willReturn(20.0);
        given(sensorDateRepository.getMaxHumidityForDateRangeAllSensors(any(), any())).willReturn(30.0);
        given(sensorDateRepository.getMaxRainfallForDateRangeAllSensors(any(), any())).willReturn(40.0);

        final StatsQueryResponseDTO response = metricsService.getStats(testUtils.generateStatsQueryRequestDTO(Arrays.asList(1L, 2L), Arrays.asList(MetricType.HUMIDITY, MetricType.RAINFALL, MetricType.TEMPERATURE, MetricType.WINDSPEED), StatType.MAX, "2023-06-01", "2023-06-07", true));
        verify(sensorDateRepository, times(1)).getMaxTemperatureForDateRangeAllSensors(any(), any());
        verify(sensorDateRepository, times(1)).getMaxTemperatureForDateRangeAllSensors(any(), any());
        verify(sensorDateRepository, times(1)).getMaxTemperatureForDateRangeAllSensors(any(), any());
        verify(sensorDateRepository, times(1)).getMaxTemperatureForDateRangeAllSensors(any(), any());

        assertThat(response.metricResponses.size()).isEqualTo(4);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.RAINFALL).metric).isEqualTo(MetricType.RAINFALL);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.RAINFALL).statType).isEqualTo(StatType.MAX);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.RAINFALL).metricValue).isEqualTo(40.0);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.WINDSPEED).metric).isEqualTo(MetricType.WINDSPEED);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.WINDSPEED).statType).isEqualTo(StatType.MAX);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.WINDSPEED).metricValue).isEqualTo(20.0);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.HUMIDITY).metric).isEqualTo(MetricType.HUMIDITY);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.HUMIDITY).statType).isEqualTo(StatType.MAX);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.HUMIDITY).metricValue).isEqualTo(30.0);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.TEMPERATURE).metric).isEqualTo(MetricType.TEMPERATURE);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.TEMPERATURE).statType).isEqualTo(StatType.MAX);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.TEMPERATURE).metricValue).isEqualTo(10.0);
    }

    @Test
    void queryMetricsAllMetricTypesSumAllSensorsSuccess() {
        given(sensorDateRepository.getSumOfTemperatureForDateRangeAllSensors(any(), any())).willReturn(10.0);
        given(sensorDateRepository.getSumOfWindspeedForDateRangeAllSensors(any(), any())).willReturn(20.0);
        given(sensorDateRepository.getSumOfHumidityForDateRangeAllSensors(any(), any())).willReturn(30.0);
        given(sensorDateRepository.getSumOfRainfallForDateRangeAllSensors(any(), any())).willReturn(40.0);

        final StatsQueryResponseDTO response = metricsService.getStats(testUtils.generateStatsQueryRequestDTO(Arrays.asList(1L, 2L), Arrays.asList(MetricType.HUMIDITY, MetricType.RAINFALL, MetricType.TEMPERATURE, MetricType.WINDSPEED), StatType.SUM, "2023-06-01", "2023-06-07", true));
        verify(sensorDateRepository, times(1)).getSumOfTemperatureForDateRangeAllSensors(any(), any());
        verify(sensorDateRepository, times(1)).getSumOfWindspeedForDateRangeAllSensors(any(), any());
        verify(sensorDateRepository, times(1)).getSumOfHumidityForDateRangeAllSensors(any(), any());
        verify(sensorDateRepository, times(1)).getSumOfRainfallForDateRangeAllSensors(any(), any());
        assertThat(response.metricResponses.size()).isEqualTo(4);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.RAINFALL).metric).isEqualTo(MetricType.RAINFALL);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.RAINFALL).statType).isEqualTo(StatType.SUM);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.RAINFALL).metricValue).isEqualTo(40.0);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.WINDSPEED).metric).isEqualTo(MetricType.WINDSPEED);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.WINDSPEED).statType).isEqualTo(StatType.SUM);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.WINDSPEED).metricValue).isEqualTo(20.0);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.HUMIDITY).metric).isEqualTo(MetricType.HUMIDITY);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.HUMIDITY).statType).isEqualTo(StatType.SUM);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.HUMIDITY).metricValue).isEqualTo(30.0);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.TEMPERATURE).metric).isEqualTo(MetricType.TEMPERATURE);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.TEMPERATURE).statType).isEqualTo(StatType.SUM);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.TEMPERATURE).metricValue).isEqualTo(10.0);
    }

    @Test
    void queryMetricsGetAllMetricsAverageForTodaySuccess() {
        given(sensorDateRepository.getAverageHumidityBySensorIdsAndDateRange(any(), any(), any())).willReturn(10.0);
        given(sensorDateRepository.getAverageWindspeedBySensorIdsAndDateRange(any(), any(), any())).willReturn(20.0);
        given(sensorDateRepository.getAverageTemperatureBySensorIdsAndDateRange(any(), any(), any())).willReturn(30.0);
        given(sensorDateRepository.getAverageRainfallBySensorIdsAndDateRange(any(), any(), any())).willReturn(40.0);

        final StatsQueryResponseDTO response = metricsService.getStats(testUtils.generateStatsQueryRequestDTO(Arrays.asList(1L, 2L), Arrays.asList(MetricType.HUMIDITY, MetricType.RAINFALL, MetricType.TEMPERATURE, MetricType.WINDSPEED), StatType.AVG, null, null, false));
        verify(sensorDateRepository, times(1)).getAverageHumidityBySensorIdsAndDateRange(any(), any(), any());
        verify(sensorDateRepository, times(1)).getAverageWindspeedBySensorIdsAndDateRange(any(), any(), any());
        verify(sensorDateRepository, times(1)).getAverageTemperatureBySensorIdsAndDateRange(any(), any(), any());
        verify(sensorDateRepository, times(1)).getAverageRainfallBySensorIdsAndDateRange(any(), any(), any());
        assertThat(response.metricResponses.size()).isEqualTo(4);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.RAINFALL).metric).isEqualTo(MetricType.RAINFALL);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.RAINFALL).statType).isEqualTo(StatType.AVG);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.RAINFALL).metricValue).isEqualTo(40.0);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.WINDSPEED).metric).isEqualTo(MetricType.WINDSPEED);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.WINDSPEED).statType).isEqualTo(StatType.AVG);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.WINDSPEED).metricValue).isEqualTo(20.0);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.HUMIDITY).metric).isEqualTo(MetricType.HUMIDITY);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.HUMIDITY).statType).isEqualTo(StatType.AVG);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.HUMIDITY).metricValue).isEqualTo(10.0);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.TEMPERATURE).metric).isEqualTo(MetricType.TEMPERATURE);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.TEMPERATURE).statType).isEqualTo(StatType.AVG);
        assertThat(response.metricResponses.get(0).getMetricResponseDTOByType(response.metricResponses, MetricType.TEMPERATURE).metricValue).isEqualTo(30.0);



    }

    @Test
    void queryMetricsDatesInTheFutureBadRequest(){
        AppException exception = Assertions.assertThrows(AppException.class, () -> {
            metricsService.getStats(testUtils.generateStatsQueryRequestDTO(Arrays.asList(1L, 2L), Arrays.asList(MetricType.HUMIDITY, MetricType.RAINFALL, MetricType.TEMPERATURE, MetricType.WINDSPEED), StatType.SUM, "2523-06-01", "2523-06-07", false));
        });

        assertThat(exception.getErrorMessage()).isEqualTo("Bad Request.  Invalid date parameters provided");
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void queryMetricsBadDateStringsBadRequest(){
        AppException exception = Assertions.assertThrows(AppException.class, () -> {
            metricsService.getStats(testUtils.generateStatsQueryRequestDTO(Arrays.asList(1L, 2L), Arrays.asList(MetricType.HUMIDITY, MetricType.RAINFALL, MetricType.TEMPERATURE, MetricType.WINDSPEED), StatType.SUM, "2023-15-35", "2023-18-77", false));
        });

        assertThat(exception.getErrorMessage()).isEqualTo("Bad Request.  Invalid date parameters provided");
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void queryMetricsWellFormattedDateStringsButEndDateLessThanStartDateBadRequest(){

        AppException exception = Assertions.assertThrows(AppException.class, () -> {
            metricsService.getStats(testUtils.generateStatsQueryRequestDTO(Arrays.asList(1L, 2L), Arrays.asList(MetricType.HUMIDITY, MetricType.RAINFALL, MetricType.TEMPERATURE, MetricType.WINDSPEED), StatType.SUM, "2023-06-06", "2023-05-01", false));
        });

        assertThat(exception.getErrorMessage()).isEqualTo("Bad Request.  Invalid date parameters provided");
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void queryMetricsNullDateBadRequest(){

        AppException exception = Assertions.assertThrows(AppException.class, () -> {
            metricsService.getStats(testUtils.generateStatsQueryRequestDTO(Arrays.asList(1L, 2L), Arrays.asList(MetricType.HUMIDITY, MetricType.RAINFALL, MetricType.TEMPERATURE, MetricType.WINDSPEED), StatType.SUM, null, "2023-06-01", false));
        });

        assertThat(exception.getErrorMessage()).isEqualTo("Bad Request.  Invalid date parameters provided");
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void queryMetricsGarbageDateBadRequest(){

        AppException exception = Assertions.assertThrows(AppException.class, () -> {
            metricsService.getStats(testUtils.generateStatsQueryRequestDTO(Arrays.asList(1L, 2L), Arrays.asList(MetricType.HUMIDITY, MetricType.RAINFALL, MetricType.TEMPERATURE, MetricType.WINDSPEED), StatType.SUM, "hello", "hello", false));
        });

        assertThat(exception.getErrorMessage()).isEqualTo("Bad Request.  Invalid date parameters provided");
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void queryMetricsNullSensorsListNullAllSensorsFlagBadRequest(){

        AppException exception = Assertions.assertThrows(AppException.class, () -> {
            metricsService.getStats(testUtils.generateStatsQueryRequestDTO(null, Arrays.asList(MetricType.HUMIDITY, MetricType.RAINFALL, MetricType.TEMPERATURE, MetricType.WINDSPEED), StatType.SUM, "hello", "hello", null));
        });

        assertThat(exception.getErrorMessage()).isEqualTo("Bad Request. Invalid sensor search options provided in query request.");
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void getAllSamplesNoSamplesInDB() {
        given(sensorDateRepository.findAll()).willReturn(new ArrayList<>());

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
