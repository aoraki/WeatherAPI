package com.jk.WeatherAPI.service;

import com.jk.WeatherAPI.dto.*;
import com.jk.WeatherAPI.dto.enums.MetricType;
import com.jk.WeatherAPI.dto.enums.StatType;
import com.jk.WeatherAPI.entities.SensorData;
import com.jk.WeatherAPI.exception.AppException;
import com.jk.WeatherAPI.repository.SensorDataRepo;
import com.jk.WeatherAPI.util.APIUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MetricsServiceImpl implements MetricsService {

    final Logger logger = LoggerFactory.getLogger(MetricsServiceImpl.class);
    final SensorDataRepo sensorDataRepo;
    final private APIUtils apiUtils = new APIUtils();

    public MetricsServiceImpl(final SensorDataRepo sensorDataRepo) {
        this.sensorDataRepo = sensorDataRepo;
    }

    public StatsQueryResponseDTO getStats(final StatsQueryRequestDTO query) {
        logger.info("Query submitted: {}", query);

        final Date startDate = apiUtils.parseStartDate(query.startDate);
        final Date endDate = apiUtils.parseEndDate(query.endDate);

        List<MetricResponseDTO> metricResponses = query.metrics.stream()
            .map(metricType -> {
                Double value = retrieveMetricValue(query.sensorIds, metricType, query.statType, startDate, endDate, false);
                return new MetricResponseDTO(query.sensorIds, metricType, query.statType, value);
            }).collect(Collectors.toList());

        final StatsQueryResponseDTO queryResponseDTO = new StatsQueryResponseDTO(metricResponses);
        logger.info("Response: {}", queryResponseDTO);
        return queryResponseDTO;
    }

    @Override
    public SensorDataDTO createSample(final SensorDataDTO sensorDataDTO) {
        logger.info("Attempting to create a metric sample with sample Id : {}", sensorDataDTO.sampleId);

        if(!isValidMetrics(sensorDataDTO)){
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

        if(!isValidMetrics(sensorDataDTO)){
            throw new AppException(400, "Bad Request");
        }

        return sensorDataRepo.findBySampleId(sensorDataDTO.sampleId)
            .map(existing -> {
                SensorData sensorDataTemp = apiUtils.convertDTOToSensorData(sensorDataDTO);
                existing.setSampleId(sensorDataTemp.getSampleId());
                existing.setSensorId(sensorDataTemp.getSensorId());
                existing.setHumidity(sensorDataTemp.getHumidity());
                existing.setTemperature(sensorDataTemp.getTemperature());
                existing.setRainfall(sensorDataTemp.getRainfall());
                existing.setWindspeed(sensorDataTemp.getWindspeed());
                SensorData sensorDataEntityUpdated = sensorDataRepo.save(existing);
                return apiUtils.convertSensorDataToDTO(sensorDataEntityUpdated);
            }).orElseThrow(() -> new AppException(404, "A sample with sample id " + sensorDataDTO.sampleId + " does not exist. Cannot update"));
    }

    @Override
    public boolean deleteSample(final Long sampleId) {
        logger.info("Attempting to delete metric sample with sample Id : {}", sampleId);

        Optional<SensorData> sensorDataEntity = Optional.ofNullable(sensorDataRepo.findBySampleId(sampleId)
                .orElseThrow(() -> new AppException(404, "Cannot find Sample with sample Id " + sampleId)));
        try {
            sensorDataRepo.delete(sensorDataEntity.get());
        } catch (Exception ex) {
            throw new AppException(500, "Unexpected error encountered deleting Metric Sample with sampleId " + sampleId);
        }
        return true;
    }

    @Override
    public List<SensorDataDTO> getAllSamples() {
        logger.info("Attempting to get all samples");

        return sensorDataRepo.findAll().stream()
                .map(apiUtils::convertSensorDataToDTO)
                .collect(Collectors.toList());
    }

    private boolean isValidMetrics(SensorDataDTO sensorDataDTO) {
        return sensorDataDTO.metrics.stream()
                .allMatch(metric -> metric.metricType != null && metric.metricValue != null);
    }

    private Double retrieveMetricValue(final List<Long> sensorIds, final MetricType metricType, final StatType statType, final Date startDate, final Date endDate, final boolean searchAllSensors) {
        switch (metricType) {
            case TEMPERATURE:
                if (statType == StatType.AVERAGE)
                    return searchAllSensors ? sensorDataRepo.getAverageTemperatureForDateRangeAllSensors(startDate, endDate) : sensorDataRepo.getAverageTemperatureBySensorIdsAndDateRange(sensorIds, startDate, endDate);
                else if (statType == StatType.MINIMUM)
                    return searchAllSensors ? sensorDataRepo.getMinTemperatureForDateRangeAllSensors(startDate, endDate) : sensorDataRepo.getMinTemperatureBySensorIdsAndDateRange(sensorIds, startDate, endDate);
                else if (statType == StatType.MAXIMUM)
                    return searchAllSensors ? sensorDataRepo.getMaxTemperatureForDateRangeAllSensors(startDate, endDate) : sensorDataRepo.getMaxTemperatureBySensorIdsAndDateRange(sensorIds, startDate, endDate);
            case WINDSPEED:
                if (statType == StatType.AVERAGE)
                    return searchAllSensors ? sensorDataRepo.getAverageWindspeedForDateRangeAllSensors(startDate, endDate) : sensorDataRepo.getAverageWindspeedBySensorIdsAndDateRange(sensorIds, startDate, endDate);
                else if (statType == StatType.MINIMUM)
                    return searchAllSensors ? sensorDataRepo.getMinWindspeedForDateRangeAllSensors(startDate, endDate) : sensorDataRepo.getMinWindspeedBySensorIdsAndDateRange(sensorIds, startDate, endDate);
                else if (statType == StatType.MAXIMUM)
                    return searchAllSensors ? sensorDataRepo.getMaxWindspeedForDateRangeAllSensors(startDate, endDate) : sensorDataRepo.getMaxWindspeedBySensorIdsAndDateRange(sensorIds, startDate, endDate);
            case HUMIDITY:
                if (statType == StatType.AVERAGE)
                    return searchAllSensors ? sensorDataRepo.getAverageHumidityForDateRangeAllSensors(startDate, endDate) : sensorDataRepo.getAverageHumidityBySensorIdsAndDateRange(sensorIds, startDate, endDate);
                else if (statType == StatType.MINIMUM)
                    return searchAllSensors ? sensorDataRepo.getMinHumidityForDateRangeAllSensors(startDate, endDate) : sensorDataRepo.getMinHumidityBySensorIdsAndDateRange(sensorIds, startDate, endDate);
                else if (statType == StatType.MAXIMUM)
                    return searchAllSensors ? sensorDataRepo.getMaxHumidityForDateRangeAllSensors(startDate, endDate) : sensorDataRepo.getMaxHumidityBySensorIdsAndDateRange(sensorIds, startDate, endDate);
            case RAINFALL:
                if (statType == StatType.AVERAGE)
                    return searchAllSensors ? sensorDataRepo.getAverageRainfallForDateRangeAllSensors(startDate, endDate) : sensorDataRepo.getAverageRainfallBySensorIdsAndDateRange(sensorIds, startDate, endDate);
                else if (statType == StatType.MINIMUM)
                    return searchAllSensors ? sensorDataRepo.getMinRainfallForDateRangeAllSensors(startDate, endDate) : sensorDataRepo.getMinRainfallBySensorIdsAndDateRange(sensorIds, startDate, endDate);
                else if (statType == StatType.MAXIMUM)
                    return searchAllSensors ? sensorDataRepo.getMaxRainfallForDateRangeAllSensors(startDate, endDate) : sensorDataRepo.getMaxRainfallBySensorIdsAndDateRange(sensorIds, startDate, endDate);
                else if (statType == StatType.SUM)
                    return searchAllSensors ? sensorDataRepo.getSumOfRainfallForDateRangeAllSensors(startDate, endDate) : sensorDataRepo.getSumOfRainfallBySensorIdsAndDateRange(sensorIds, startDate, endDate);
        }
        return 0.0;
    }
}
