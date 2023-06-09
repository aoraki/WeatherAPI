package com.jk.WeatherAPI.entities;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "sensor_data", indexes = @Index(columnList = "sensorId"))
public class SensorData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true)
    private Long sampleId;

    private Long sensorId;
    private Timestamp sampleTime;
    private double temperature;
    private double rainfall;
    private double windspeed;
    private double humidity;

    public SensorData(Long sampleId, Long sensorId, Timestamp sampleTime, double temperature, double rainfall, double windspeed, double humidity) {
        this.sampleId = sampleId;
        this.sensorId = sensorId;
        this.sampleTime = sampleTime;
        this.temperature = temperature;
        this.rainfall = rainfall;
        this.windspeed = windspeed;
        this.humidity = humidity;
    }

    public SensorData() {
    }

    public Long getSampleId() {
        return sampleId;
    }

    public void setSampleId(Long sampleId) {
        this.sampleId = sampleId;
    }

    public Long getSensorId() {
        return sensorId;
    }

    public void setSensorId(Long sensorId) {
        this.sensorId = sensorId;
    }

    public Timestamp getSampleTime() {
        return sampleTime;
    }

    public void setSampleTime(Timestamp sampleTime) {
        this.sampleTime = sampleTime;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getRainfall() {
        return rainfall;
    }

    public void setRainfall(double rainfall) {
        this.rainfall = rainfall;
    }

    public double getWindspeed() {
        return windspeed;
    }

    public void setWindspeed(double windspeed) {
        this.windspeed = windspeed;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

}
