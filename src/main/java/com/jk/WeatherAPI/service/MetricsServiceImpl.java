package com.jk.WeatherAPI.service;

import com.jk.WeatherAPI.dto.*;
import com.jk.WeatherAPI.dto.enums.MetricType;
import com.jk.WeatherAPI.dto.enums.StatType;
import com.jk.WeatherAPI.entities.SensorData;
import com.jk.WeatherAPI.exception.AppException;
import com.jk.WeatherAPI.repository.SensorDataRepo;
import com.jk.WeatherAPI.util.APIUtils;
import com.jk.WeatherAPI.util.Validation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MetricsServiceImpl implements MetricsService {

    final Logger logger = LoggerFactory.getLogger(MetricsServiceImpl.class);
    final SensorDataRepo sensorDataRepo;
    final private APIUtils apiUtils = new APIUtils();
    final private Validation validation = new Validation();

    public MetricsServiceImpl(final SensorDataRepo sensorDataRepo) {
        this.sensorDataRepo = sensorDataRepo;
    }

    public StatsQueryResponseDTO getStats(final StatsQueryRequestDTO query) {
        logger.info("Query submitted: {}", query);

        if(!validation.isValidSensors(query)){
            throw new AppException(400, "Bad Request. Invalid sensor search options provided in query request.");
        }

        List<MetricResponseDTO> metricResponses;
        if(validation.isObtainLatestMetrics(query)){
            metricResponses = getMetricResponseDTOS(query, validation.getSearchDateForToday(false), validation.getSearchDateForToday(true));
        } else {
            if(validation.isValidDates(query)){
                final Date startDate = validation.parseQueryDate(query.startDate, false);
                final Date endDate = validation.parseQueryDate(query.endDate, true);
                metricResponses = getMetricResponseDTOS(query, startDate, endDate);
            } else {
                throw new AppException(400, "Bad Request.  Invalid date parameters provided");
            }
        }
        final StatsQueryResponseDTO queryResponseDTO = new StatsQueryResponseDTO(metricResponses);

        logger.info("Response: {}", queryResponseDTO);
        return queryResponseDTO;
    }

    private List<MetricResponseDTO> getMetricResponseDTOS(final StatsQueryRequestDTO query, final Date startDate, final Date endDate) {
        List<MetricResponseDTO> metricResponses;
        metricResponses = query.metrics.stream()
            .map(metricType -> {
                Double value = retrieveMetricValue(query.sensorIds, metricType, query.statType, startDate, endDate, query.searchAllSensors != null ? query.searchAllSensors : false);
                return new MetricResponseDTO(metricType, query.statType, value);
            }).collect(Collectors.toList());
        return metricResponses;
    }

    @Override
    public SensorDataDTO createSample(final SensorDataDTO sensorDataDTO) {
        logger.info("Attempting to create a metric sample with sample Id : {}", sensorDataDTO.sampleId);

        if(!validation.isValidMetrics(sensorDataDTO)){
            throw new AppException(400, "Bad Request");
        }

        sensorDataRepo.findBySampleId(sensorDataDTO.sampleId)
            .ifPresent(data -> {
                throw new AppException(409, "A sample with sampleId " + sensorDataDTO.sampleId + " already exists");
            });
        final SensorData sensorDataEntityNew = sensorDataRepo.save(apiUtils.convertDTOToSensorData(sensorDataDTO));
        return apiUtils.convertSensorDataToDTO(sensorDataEntityNew);
    }

    @Override
    public SensorDataDTO getSampleBySampleId(final Long sampleId) {
        logger.info("Attempting to get a metric sample with sample Id : {}", sampleId);
        return sensorDataRepo.findBySampleId(sampleId)
            .map(apiUtils::convertSensorDataToDTO)
            .orElseThrow(() -> new AppException(404, "Cannot find Sample with sample Id " + sampleId));
    }

    @Override
    public SensorDataDTO updateSample(final SensorDataDTO sensorDataDTO) {
        logger.info("Attempting to update a metric sample with sample Id : {}", sensorDataDTO.sampleId);

        if(!validation.isValidMetrics(sensorDataDTO)){
            throw new AppException(400, "Bad Request");
        }

        return sensorDataRepo.findBySampleId(sensorDataDTO.sampleId)
            .map(existing -> {
                SensorData sensorDataTemp = apiUtils.convertDTOToSensorData(sensorDataDTO);

                existing.setSampleId(sensorDataTemp.getSampleId() != null ? sensorDataTemp.getSampleId() : existing.getSampleId());
                existing.setSensorId(sensorDataTemp.getSensorId() != null ? sensorDataTemp.getSensorId() : existing.getSensorId());
                existing.setHumidity(sensorDataTemp.getHumidity() != null ? sensorDataTemp.getHumidity() : existing.getHumidity());
                existing.setTemperature(sensorDataTemp.getTemperature() != null ? sensorDataTemp.getTemperature() : existing.getTemperature());
                existing.setRainfall(sensorDataTemp.getRainfall() != null ? sensorDataTemp.getRainfall() : existing.getRainfall());
                existing.setWindspeed(sensorDataTemp.getWindspeed() != null ? sensorDataTemp.getWindspeed() : existing.getWindspeed());

                SensorData sensorDataEntityUpdated = sensorDataRepo.save(existing);
                return apiUtils.convertSensorDataToDTO(sensorDataEntityUpdated);
            }).orElseThrow(() -> new AppException(404, "A sample with sample id " + sensorDataDTO.sampleId + " does not exist. Cannot update"));
    }

    @Override
    public boolean deleteSample(final Long sampleId) {
        logger.info("Attempting to delete metric sample with sample Id : {}", sampleId);

        Optional<SensorData> sensorDataEntity = Optional.ofNullable(sensorDataRepo.findBySampleId(sampleId)
                .orElseThrow(() -> new AppException(404, "Cannot find Sample with sample Id " + sampleId)));

        sensorDataRepo.delete(sensorDataEntity.get());
        return true;
    }

    @Override
    public List<SensorDataDTO> getAllSamples() {
        logger.info("Attempting to get all samples");
        return sensorDataRepo.findAll().stream()
                .map(apiUtils::convertSensorDataToDTO)
                .collect(Collectors.toList());
    }

    private Double retrieveMetricValue(final List<Long> sensorIds, final MetricType metricType, final StatType statType, final Date startDate, final Date endDate, final boolean searchAllSensors) {
        switch (metricType) {
            case TEMPERATURE:
                if (statType == StatType.AVG)
                    return searchAllSensors ? sensorDataRepo.getAverageTemperatureForDateRangeAllSensors(startDate, endDate) : sensorDataRepo.getAverageTemperatureBySensorIdsAndDateRange(sensorIds, startDate, endDate);
                else if (statType == StatType.MIN)
                    return searchAllSensors ? sensorDataRepo.getMinTemperatureForDateRangeAllSensors(startDate, endDate) : sensorDataRepo.getMinTemperatureBySensorIdsAndDateRange(sensorIds, startDate, endDate);
                else if (statType == StatType.MAX)
                    return searchAllSensors ? sensorDataRepo.getMaxTemperatureForDateRangeAllSensors(startDate, endDate) : sensorDataRepo.getMaxTemperatureBySensorIdsAndDateRange(sensorIds, startDate, endDate);
                else if (statType == StatType.SUM)
                    return searchAllSensors ? sensorDataRepo.getSumOfTemperatureForDateRangeAllSensors(startDate, endDate) : sensorDataRepo.getSumOfTemperatureBySensorIdsAndDateRange(sensorIds, startDate, endDate);
            case WINDSPEED:
                if (statType == StatType.AVG)
                    return searchAllSensors ? sensorDataRepo.getAverageWindspeedForDateRangeAllSensors(startDate, endDate) : sensorDataRepo.getAverageWindspeedBySensorIdsAndDateRange(sensorIds, startDate, endDate);
                else if (statType == StatType.MIN)
                    return searchAllSensors ? sensorDataRepo.getMinWindspeedForDateRangeAllSensors(startDate, endDate) : sensorDataRepo.getMinWindspeedBySensorIdsAndDateRange(sensorIds, startDate, endDate);
                else if (statType == StatType.MAX)
                    return searchAllSensors ? sensorDataRepo.getMaxWindspeedForDateRangeAllSensors(startDate, endDate) : sensorDataRepo.getMaxWindspeedBySensorIdsAndDateRange(sensorIds, startDate, endDate);
                else if (statType == StatType.SUM)
                    return searchAllSensors ? sensorDataRepo.getSumOfWindspeedForDateRangeAllSensors(startDate, endDate) : sensorDataRepo.getSumOfWindspeedBySensorIdsAndDateRange(sensorIds, startDate, endDate);
            case HUMIDITY:
                if (statType == StatType.AVG)
                    return searchAllSensors ? sensorDataRepo.getAverageHumidityForDateRangeAllSensors(startDate, endDate) : sensorDataRepo.getAverageHumidityBySensorIdsAndDateRange(sensorIds, startDate, endDate);
                else if (statType == StatType.MIN)
                    return searchAllSensors ? sensorDataRepo.getMinHumidityForDateRangeAllSensors(startDate, endDate) : sensorDataRepo.getMinHumidityBySensorIdsAndDateRange(sensorIds, startDate, endDate);
                else if (statType == StatType.MAX)
                    return searchAllSensors ? sensorDataRepo.getMaxHumidityForDateRangeAllSensors(startDate, endDate) : sensorDataRepo.getMaxHumidityBySensorIdsAndDateRange(sensorIds, startDate, endDate);
                else if (statType == StatType.SUM)
                    return searchAllSensors ? sensorDataRepo.getSumOfHumidityForDateRangeAllSensors(startDate, endDate) : sensorDataRepo.getSumOfHumidityBySensorIdsAndDateRange(sensorIds, startDate, endDate);
            case RAINFALL:
                if (statType == StatType.AVG)
                    return searchAllSensors ? sensorDataRepo.getAverageRainfallForDateRangeAllSensors(startDate, endDate) : sensorDataRepo.getAverageRainfallBySensorIdsAndDateRange(sensorIds, startDate, endDate);
                else if (statType == StatType.MIN)
                    return searchAllSensors ? sensorDataRepo.getMinRainfallForDateRangeAllSensors(startDate, endDate) : sensorDataRepo.getMinRainfallBySensorIdsAndDateRange(sensorIds, startDate, endDate);
                else if (statType == StatType.MAX)
                    return searchAllSensors ? sensorDataRepo.getMaxRainfallForDateRangeAllSensors(startDate, endDate) : sensorDataRepo.getMaxRainfallBySensorIdsAndDateRange(sensorIds, startDate, endDate);
                else if (statType == StatType.SUM)
                    return searchAllSensors ? sensorDataRepo.getSumOfRainfallForDateRangeAllSensors(startDate, endDate) : sensorDataRepo.getSumOfRainfallBySensorIdsAndDateRange(sensorIds, startDate, endDate);
        }
        return 0.0;
    }
}
