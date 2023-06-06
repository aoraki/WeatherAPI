package com.jk.WeatherAPI.repository;

import com.jk.WeatherAPI.entities.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface SensorDataRepo extends JpaRepository<SensorData, Long> {

    // Temperature Queries
    @Query("SELECT AVG(s.temperature) FROM SensorData s WHERE s.sensorId IN :sensorIds AND s.sampleTime BETWEEN :startDate AND :endDate")
    Double getAverageTemperatureBySensorIdsAndDateRange(List<Long> sensorIds, Date startDate, Date endDate);

    @Query("SELECT MAX(s.temperature) FROM SensorData s WHERE s.sensorId IN :sensorIds AND s.sampleTime BETWEEN :startDate AND :endDate")
    Double getMaxTemperatureBySensorIdsAndDateRange(List<Long> sensorIds, Date startDate, Date endDate);

    @Query("SELECT MIN(s.temperature) FROM SensorData s WHERE s.sensorId IN :sensorIds AND s.sampleTime BETWEEN :startDate AND :endDate")
    Double getMinTemperatureBySensorIdsAndDateRange(List<Long> sensorIds, Date startDate, Date endDate);

    // Windspeed Queries
    @Query("SELECT AVG(s.windspeed) FROM SensorData s WHERE s.sensorId IN :sensorIds AND s.sampleTime BETWEEN :startDate AND :endDate")
    Double getAverageWindspeedBySensorIdsAndDateRange(List<Long> sensorIds, Date startDate, Date endDate);

    @Query("SELECT MAX(s.windspeed) FROM SensorData s WHERE s.sensorId IN :sensorIds AND s.sampleTime BETWEEN :startDate AND :endDate")
    Double getMaxWindspeedBySensorIdsAndDateRange(List<Long> sensorIds, Date startDate, Date endDate);

    @Query("SELECT MIN(s.windspeed) FROM SensorData s WHERE s.sensorId IN :sensorIds AND s.sampleTime BETWEEN :startDate AND :endDate")
    Double getMinWindspeedBySensorIdsAndDateRange(List<Long> sensorIds, Date startDate, Date endDate);

    // Humidity Queries
    @Query("SELECT AVG(s.humidity) FROM SensorData s WHERE s.sensorId IN :sensorIds AND s.sampleTime BETWEEN :startDate AND :endDate")
    Double getAverageHumidityBySensorIdsAndDateRange(List<Long> sensorIds, Date startDate, Date endDate);

    @Query("SELECT MAX(s.humidity) FROM SensorData s WHERE s.sensorId IN :sensorIds AND s.sampleTime BETWEEN :startDate AND :endDate")
    Double getMaxHumidityBySensorIdsAndDateRange(List<Long> sensorIds, Date startDate, Date endDate);

    @Query("SELECT MIN(s.humidity) FROM SensorData s WHERE s.sensorId IN :sensorIds AND s.sampleTime BETWEEN :startDate AND :endDate")
    Double getMinHumidityBySensorIdsAndDateRange(List<Long> sensorIds, Date startDate, Date endDate);

    // Rainfall Queries
    @Query("SELECT AVG(s.rainfall) FROM SensorData s WHERE s.sensorId IN :sensorIds AND s.sampleTime BETWEEN :startDate AND :endDate")
    Double getAverageRainfallBySensorIdsAndDateRange(List<Long> sensorIds, Date startDate, Date endDate);

    @Query("SELECT MAX(s.rainfall) FROM SensorData s WHERE s.sensorId IN :sensorIds AND s.sampleTime BETWEEN :startDate AND :endDate")
    Double getMaxRainfallBySensorIdsAndDateRange(List<Long> sensorIds, Date startDate, Date endDate);

    @Query("SELECT MIN(s.rainfall) FROM SensorData s WHERE s.sensorId IN :sensorIds AND s.sampleTime BETWEEN :startDate AND :endDate")
    Double getMinRainfallBySensorIdsAndDateRange(List<Long> sensorIds, Date startDate, Date endDate);

    @Query("SELECT SUM(s.rainfall) FROM SensorData s WHERE s.sensorId IN :sensorIds AND s.sampleTime BETWEEN :startDate AND :endDate")
    Double getSumOfRainfallBySensorIdsAndDateRange(List<Long> sensorIds, Date startDate, Date endDate);
}



