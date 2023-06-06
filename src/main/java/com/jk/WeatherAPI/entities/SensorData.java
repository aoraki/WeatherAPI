package com.jk.WeatherAPI.entities;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "sensor_data", indexes = @Index(columnList = "sensorId"))
public class SensorData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sampleId;

    private Long sensorId;
    private Timestamp sampleTime;
    private double temperature;
    private double rainfall;
    private double windspeed;
    private double humidity;

    public SensorData(Long sensorId, Timestamp sampleTime, double temperature, double rainfall, double windspeed, double humidity) {
        this.sensorId = sensorId;
        this.sampleTime = sampleTime;
        this.temperature = temperature;
        this.rainfall = rainfall;
        this.windspeed = windspeed;
        this.humidity = humidity;
    }
}
