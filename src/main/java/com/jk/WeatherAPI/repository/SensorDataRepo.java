package com.jk.WeatherAPI.repository;

import com.jk.WeatherAPI.entities.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface SensorDataRepo extends JpaRepository<SensorData, Long> {

    // Custom CRUD query
    Optional<SensorData> findBySampleId(Long sampleId);

    // Temperature Queries
    @Query("SELECT AVG(s.temperature) FROM SensorData s WHERE s.sensorId IN :sensorIds AND s.sampleTime BETWEEN :startDate AND :endDate")
    Double getAverageTemperatureBySensorIdsAndDateRange(List<Long> sensorIds, Date startDate, Date endDate);

    @Query("SELECT MAX(s.temperature) FROM SensorData s WHERE s.sensorId IN :sensorIds AND s.sampleTime BETWEEN :startDate AND :endDate")
    Double getMaxTemperatureBySensorIdsAndDateRange(List<Long> sensorIds, Date startDate, Date endDate);

    @Query("SELECT MIN(s.temperature) FROM SensorData s WHERE s.sensorId IN :sensorIds AND s.sampleTime BETWEEN :startDate AND :endDate")
    Double getMinTemperatureBySensorIdsAndDateRange(List<Long> sensorIds, Date startDate, Date endDate);

    @Query("SELECT SUM(s.temperature) FROM SensorData s WHERE s.sensorId IN :sensorIds AND s.sampleTime BETWEEN :startDate AND :endDate")
    Double getSumOfTemperatureBySensorIdsAndDateRange(List<Long> sensorIds, Date startDate, Date endDate);

    @Query("SELECT AVG(s.temperature) FROM SensorData s WHERE s.sampleTime BETWEEN :startDate AND :endDate")
    Double getAverageTemperatureForDateRangeAllSensors(Date startDate, Date endDate);

    @Query("SELECT MAX(s.temperature) FROM SensorData s WHERE s.sampleTime BETWEEN :startDate AND :endDate")
    Double getMaxTemperatureForDateRangeAllSensors(Date startDate, Date endDate);

    @Query("SELECT MIN(s.temperature) FROM SensorData s WHERE s.sampleTime BETWEEN :startDate AND :endDate")
    Double getMinTemperatureForDateRangeAllSensors(Date startDate, Date endDate);

    @Query("SELECT SUM(s.temperature) FROM SensorData s WHERE s.sampleTime BETWEEN :startDate AND :endDate")
    Double getSumOfTemperatureForDateRangeAllSensors(Date startDate, Date endDate);

    // Windspeed Queries
    @Query("SELECT AVG(s.windspeed) FROM SensorData s WHERE s.sensorId IN :sensorIds AND s.sampleTime BETWEEN :startDate AND :endDate")
    Double getAverageWindspeedBySensorIdsAndDateRange(List<Long> sensorIds, Date startDate, Date endDate);

    @Query("SELECT MAX(s.windspeed) FROM SensorData s WHERE s.sensorId IN :sensorIds AND s.sampleTime BETWEEN :startDate AND :endDate")
    Double getMaxWindspeedBySensorIdsAndDateRange(List<Long> sensorIds, Date startDate, Date endDate);

    @Query("SELECT MIN(s.windspeed) FROM SensorData s WHERE s.sensorId IN :sensorIds AND s.sampleTime BETWEEN :startDate AND :endDate")
    Double getMinWindspeedBySensorIdsAndDateRange(List<Long> sensorIds, Date startDate, Date endDate);

    @Query("SELECT SUM(s.windspeed) FROM SensorData s WHERE s.sensorId IN :sensorIds AND s.sampleTime BETWEEN :startDate AND :endDate")
    Double getSumOfWindspeedBySensorIdsAndDateRange(List<Long> sensorIds, Date startDate, Date endDate);

    @Query("SELECT AVG(s.windspeed) FROM SensorData s WHERE s.sampleTime BETWEEN :startDate AND :endDate")
    Double getAverageWindspeedForDateRangeAllSensors(Date startDate, Date endDate);

    @Query("SELECT MAX(s.windspeed) FROM SensorData s WHERE s.sampleTime BETWEEN :startDate AND :endDate")
    Double getMaxWindspeedForDateRangeAllSensors(Date startDate, Date endDate);

    @Query("SELECT MIN(s.windspeed) FROM SensorData s WHERE s.sampleTime BETWEEN :startDate AND :endDate")
    Double getMinWindspeedForDateRangeAllSensors(Date startDate, Date endDate);

    @Query("SELECT SUM(s.windspeed) FROM SensorData s WHERE s.sampleTime BETWEEN :startDate AND :endDate")
    Double getSumOfWindspeedForDateRangeAllSensors(Date startDate, Date endDate);

    // Humidity Queries
    @Query("SELECT AVG(s.humidity) FROM SensorData s WHERE s.sensorId IN :sensorIds AND s.sampleTime BETWEEN :startDate AND :endDate")
    Double getAverageHumidityBySensorIdsAndDateRange(List<Long> sensorIds, Date startDate, Date endDate);

    @Query("SELECT MAX(s.humidity) FROM SensorData s WHERE s.sensorId IN :sensorIds AND s.sampleTime BETWEEN :startDate AND :endDate")
    Double getMaxHumidityBySensorIdsAndDateRange(List<Long> sensorIds, Date startDate, Date endDate);

    @Query("SELECT MIN(s.humidity) FROM SensorData s WHERE s.sensorId IN :sensorIds AND s.sampleTime BETWEEN :startDate AND :endDate")
    Double getMinHumidityBySensorIdsAndDateRange(List<Long> sensorIds, Date startDate, Date endDate);

    @Query("SELECT SUM(s.humidity) FROM SensorData s WHERE s.sensorId IN :sensorIds AND s.sampleTime BETWEEN :startDate AND :endDate")
    Double getSumOfHumidityBySensorIdsAndDateRange(List<Long> sensorIds, Date startDate, Date endDate);

    @Query("SELECT AVG(s.humidity) FROM SensorData s WHERE s.sampleTime BETWEEN :startDate AND :endDate")
    Double getAverageHumidityForDateRangeAllSensors(Date startDate, Date endDate);

    @Query("SELECT MAX(s.humidity) FROM SensorData s WHERE s.sampleTime BETWEEN :startDate AND :endDate")
    Double getMaxHumidityForDateRangeAllSensors(Date startDate, Date endDate);

    @Query("SELECT MIN(s.humidity) FROM SensorData s WHERE s.sampleTime BETWEEN :startDate AND :endDate")
    Double getMinHumidityForDateRangeAllSensors(Date startDate, Date endDate);

    @Query("SELECT SUM(s.humidity) FROM SensorData s WHERE s.sampleTime BETWEEN :startDate AND :endDate")
    Double getSumOfHumidityForDateRangeAllSensors(Date startDate, Date endDate);

    // Rainfall Queries
    @Query("SELECT AVG(s.rainfall) FROM SensorData s WHERE s.sensorId IN :sensorIds AND s.sampleTime BETWEEN :startDate AND :endDate")
    Double getAverageRainfallBySensorIdsAndDateRange(List<Long> sensorIds, Date startDate, Date endDate);

    @Query("SELECT MAX(s.rainfall) FROM SensorData s WHERE s.sensorId IN :sensorIds AND s.sampleTime BETWEEN :startDate AND :endDate")
    Double getMaxRainfallBySensorIdsAndDateRange(List<Long> sensorIds, Date startDate, Date endDate);

    @Query("SELECT MIN(s.rainfall) FROM SensorData s WHERE s.sensorId IN :sensorIds AND s.sampleTime BETWEEN :startDate AND :endDate")
    Double getMinRainfallBySensorIdsAndDateRange(List<Long> sensorIds, Date startDate, Date endDate);

    @Query("SELECT SUM(s.rainfall) FROM SensorData s WHERE s.sensorId IN :sensorIds AND s.sampleTime BETWEEN :startDate AND :endDate")
    Double getSumOfRainfallBySensorIdsAndDateRange(List<Long> sensorIds, Date startDate, Date endDate);

    @Query("SELECT AVG(s.rainfall) FROM SensorData s WHERE s.sampleTime BETWEEN :startDate AND :endDate")
    Double getAverageRainfallForDateRangeAllSensors(Date startDate, Date endDate);

    @Query("SELECT MAX(s.rainfall) FROM SensorData s WHERE s.sampleTime BETWEEN :startDate AND :endDate")
    Double getMaxRainfallForDateRangeAllSensors(Date startDate, Date endDate);

    @Query("SELECT MIN(s.rainfall) FROM SensorData s WHERE s.sampleTime BETWEEN :startDate AND :endDate")
    Double getMinRainfallForDateRangeAllSensors(Date startDate, Date endDate);

    @Query("SELECT SUM(s.rainfall) FROM SensorData s WHERE s.sampleTime BETWEEN :startDate AND :endDate")
    Double getSumOfRainfallForDateRangeAllSensors(Date startDate, Date endDate);
}



