package com.jk.WeatherAPI.entities;

import java.sql.Timestamp;

public class SensorDataBuilder {
    private Long sensorId;
    private Timestamp sampleTime;
    private double temperature;
    private double rainfall;
    private double windspeed;
    private double humidity;

    public SensorDataBuilder setSensorId(Long sensorId) {
        this.sensorId = sensorId;
        return this;
    }

    public SensorDataBuilder setSampleTime(Timestamp sampleTime) {
        this.sampleTime = sampleTime;
        return this;
    }

    public SensorDataBuilder setTemperature(double temperature) {
        this.temperature = temperature;
        return this;
    }

    public SensorDataBuilder setRainfall(double rainfall) {
        this.rainfall = rainfall;
        return this;
    }

    public SensorDataBuilder setWindspeed(double windspeed) {
        this.windspeed = windspeed;
        return this;
    }

    public SensorDataBuilder setHumidity(double humidity) {
        this.humidity = humidity;
        return this;
    }

    public SensorData createSensorData() {
        return new SensorData(sensorId, sampleTime, temperature, rainfall, windspeed, humidity);
    }
}