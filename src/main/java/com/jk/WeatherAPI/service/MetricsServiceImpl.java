package com.jk.WeatherAPI.service;

import com.jk.WeatherAPI.dto.*;
import com.jk.WeatherAPI.dto.enums.MetricType;
import com.jk.WeatherAPI.dto.enums.StatType;
import com.jk.WeatherAPI.entities.SensorData;
import com.jk.WeatherAPI.repository.SensorDataRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MetricsServiceImpl implements MetricsService {

    Logger logger = LoggerFactory.getLogger(MetricsServiceImpl.class);

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private SensorDataRepo sensorDataRepo;

    public MetricsServiceImpl(final SensorDataRepo sensorDataRepo) {
        this.sensorDataRepo = sensorDataRepo;
    }

    @Override
    public StatsQueryResponseDTO getStats(final StatsQueryRequestDTO query) {
        logger.info("Metrics submitted : {}", query);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1); // Subtract 1 day
        Date oneDayAgo = calendar.getTime();

        Date startDate = parseDateTime(query.getStartDate(), oneDayAgo);
        Date endDate = parseDateTime(query.getEndDate(), new Date());

        List<MetricResponseDTO> metricResponses = query.getMetrics().stream()
            .map(metricType -> {
                Double value = 0.0;
                if (metricType.equals(MetricType.TEMPERATURE)) {
                    if (query.getStatType().equals(StatType.AVERAGE)) {
                        value = sensorDataRepo.getAverageTemperatureBySensorIdsAndDateRange(query.getSensorIds(), startDate, endDate);
                    } else if (query.getStatType().equals(StatType.MINIMUM)) {
                        value = sensorDataRepo.getMinTemperatureBySensorIdsAndDateRange(query.getSensorIds(), startDate, endDate);
                    } else if (query.getStatType().equals(StatType.MAXIMUM)) {
                        value = sensorDataRepo.getMaxTemperatureBySensorIdsAndDateRange(query.getSensorIds(), startDate, endDate);
                    }
                } else if (metricType.equals(MetricType.WINDSPEED)) {
                    if (query.getStatType().equals(StatType.AVERAGE)) {
                        value = sensorDataRepo.getAverageWindspeedBySensorIdsAndDateRange(query.getSensorIds(), startDate, endDate);
                    } else if (query.getStatType().equals(StatType.MINIMUM)) {
                        value = sensorDataRepo.getMinWindspeedBySensorIdsAndDateRange(query.getSensorIds(), startDate, endDate);
                    } else if (query.getStatType().equals(StatType.MAXIMUM)) {
                        value = sensorDataRepo.getMaxWindspeedBySensorIdsAndDateRange(query.getSensorIds(), startDate, endDate);
                    }
                } else if (metricType.equals(MetricType.HUMIDITY)) {
                    if (query.getStatType().equals(StatType.AVERAGE)) {
                        value = sensorDataRepo.getAverageHumidityBySensorIdsAndDateRange(query.getSensorIds(), startDate, endDate);
                    } else if (query.getStatType().equals(StatType.MINIMUM)) {
                        value = sensorDataRepo.getMinHumidityBySensorIdsAndDateRange(query.getSensorIds(), startDate, endDate);
                    } else if (query.getStatType().equals(StatType.MAXIMUM)) {
                        value = sensorDataRepo.getMaxHumidityBySensorIdsAndDateRange(query.getSensorIds(), startDate, endDate);
                    }
                } else if (metricType.equals(MetricType.RAINFALL)) {
                    if (query.getStatType().equals(StatType.AVERAGE)) {
                        value = sensorDataRepo.getAverageRainfallBySensorIdsAndDateRange(query.getSensorIds(), startDate, endDate);
                    } else if (query.getStatType().equals(StatType.MINIMUM)) {
                        value = sensorDataRepo.getMinRainfallBySensorIdsAndDateRange(query.getSensorIds(), startDate, endDate);
                    } else if (query.getStatType().equals(StatType.MAXIMUM)) {
                        value = sensorDataRepo.getMaxRainfallBySensorIdsAndDateRange(query.getSensorIds(), startDate, endDate);
                    } else if (query.getStatType().equals(StatType.SUM)) {
                        value = sensorDataRepo.getSumOfRainfallBySensorIdsAndDateRange(query.getSensorIds(), startDate, endDate);
                    }
                }
                return new MetricResponseDTO(query.getSensorIds(), metricType, query.getStatType(), value);
            })
            .collect(Collectors.toList());

        StatsQueryResponseDTO queryResponseDTO = new StatsQueryResponseDTO(metricResponses);

        //Double sumTemp = sensorDataRepo.getSumOfTemperaturesBySensorIdsAndDateRange(query.getSensorIds(), startDate, endDate);
        logger.info("Sum Temp : {}", queryResponseDTO);
        return queryResponseDTO;
    }

    @Override
    public boolean submitMetrics(final SensorDataDTO sensorData) {
        logger.info("Metrics submitted : {}", sensorData);
        sensorDataRepo.save(convertDTOToSensorData(sensorData));
        return true;
    }

    public SensorData convertDTOToSensorData(final SensorDataDTO sensorData){
        Double temperature = sensorData.getMetrics().stream()
                .filter(metric -> metric.getMetricName().equals(MetricType.TEMPERATURE.value))
                .map(MetricDTO::getMetricValue)
                .findFirst()
                .orElse(0.0);

        Double rainfall = sensorData.getMetrics().stream()
                .filter(metric -> metric.getMetricName().equals(MetricType.RAINFALL.value))
                .map(MetricDTO::getMetricValue)
                .findFirst()
                .orElse(0.0);

        Double windspeed = sensorData.getMetrics().stream()
                .filter(metric -> metric.getMetricName().equals(MetricType.WINDSPEED.value))
                .map(MetricDTO::getMetricValue)
                .findFirst()
                .orElse(0.0);

        Double humidity = sensorData.getMetrics().stream()
                .filter(metric -> metric.getMetricName().equals(MetricType.HUMIDITY.value))
                .map(MetricDTO::getMetricValue)
                .findFirst()
                .orElse(0.0);

        return new SensorData(sensorData.getSensorId(), Timestamp. valueOf(LocalDateTime.now()), temperature, rainfall,windspeed, humidity);
    }

    private Date parseDateTime(String dateStr, Date defaultValue) {
        try {
            Date date = dateFormat.parse(dateStr);
            return date;
        } catch (ParseException e) {
            logger.error("Error occurred while parsing the date string: " + dateStr);
            return defaultValue;
        }
    }
}
