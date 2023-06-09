package com.jk.WeatherAPI.util;

import com.jk.WeatherAPI.dto.MetricDTO;
import com.jk.WeatherAPI.dto.SensorDataDTO;
import com.jk.WeatherAPI.dto.enums.MetricType;
import com.jk.WeatherAPI.entities.SensorData;
import com.jk.WeatherAPI.service.MetricsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class APIUtils {

    final Logger logger = LoggerFactory.getLogger(MetricsServiceImpl.class);
    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public SensorData convertDTOToSensorData(final SensorDataDTO sensorData) {
        Double temperature = getMetricValue(sensorData, MetricType.TEMPERATURE);
        Double rainfall = getMetricValue(sensorData, MetricType.RAINFALL);
        Double windspeed = getMetricValue(sensorData, MetricType.WINDSPEED);
        Double humidity = getMetricValue(sensorData, MetricType.HUMIDITY);
        return new SensorData(sensorData.sampleId, sensorData.sensorId, Timestamp.valueOf(LocalDateTime.now()), temperature, rainfall, windspeed, humidity);
    }

    public SensorDataDTO convertSensorDataToDTO(final SensorData sensorData) {
        List<MetricDTO> metricDTOs = new ArrayList<>();
        metricDTOs.add(this.generateMetricDTO(MetricType.TEMPERATURE, sensorData.getTemperature()));
        metricDTOs.add(this.generateMetricDTO(MetricType.WINDSPEED, sensorData.getWindspeed()));
        metricDTOs.add(this.generateMetricDTO(MetricType.HUMIDITY, sensorData.getHumidity()));
        metricDTOs.add(this.generateMetricDTO(MetricType.RAINFALL, sensorData.getRainfall()));
        return new SensorDataDTO(sensorData.getSampleId(), sensorData.getSensorId(), metricDTOs);
    }

    private MetricDTO generateMetricDTO(final MetricType metricType, final Double metricValue){
        return new MetricDTO(metricType, metricValue);
    }

    public Double getMetricValue(final SensorDataDTO sensorData, final MetricType metricType) {
        return sensorData.metrics.stream()
                .filter(metric -> metric.metricType.equals(metricType))
                .map(metric -> metric.metricValue)
                .findFirst()
                .orElse(0.0);
    }

    public Date parseStartDate(final String startDateStr) {
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        final Date oneDayAgo = calendar.getTime();
        return parseDateTime(startDateStr, oneDayAgo);
    }

    public Date parseEndDate(final String endDateStr) {
        return parseDateTime(endDateStr, new Date());
    }

    public Date parseDateTime(final String dateStr, final Date defaultValue) {
        try {
            final Date date = dateFormat.parse(dateStr);
            return date;
        } catch (ParseException e) {
            logger.error("Error occurred while parsing the date string: " + dateStr);
            return defaultValue;
        }
    }
}
