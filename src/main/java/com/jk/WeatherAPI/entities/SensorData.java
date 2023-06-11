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
    private Double temperature;
    private Double rainfall;
    private Double windspeed;
    private Double humidity;

    public SensorData(final Long sampleId, final Long sensorId, final Timestamp sampleTime, final Double temperature, final Double rainfall, final Double windspeed, final Double humidity) {
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

    public void setSampleId(final Long sampleId) {
        this.sampleId = sampleId;
    }

    public Long getSensorId() {
        return sensorId;
    }

    public void setSensorId(final Long sensorId) {
        this.sensorId = sensorId;
    }

    public Timestamp getSampleTime() {
        return sampleTime;
    }

    public void setSampleTime(final Timestamp sampleTime) {
        this.sampleTime = sampleTime;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(final Double temperature) {
        this.temperature = temperature;
    }

    public Double getRainfall() {
        return rainfall;
    }

    public void setRainfall(final Double rainfall) {
        this.rainfall = rainfall;
    }

    public Double getWindspeed() {
        return windspeed;
    }

    public void setWindspeed(final Double windspeed) {
        this.windspeed = windspeed;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(final Double humidity) {
        this.humidity = humidity;
    }
}
