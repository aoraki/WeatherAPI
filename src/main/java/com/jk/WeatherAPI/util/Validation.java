package com.jk.WeatherAPI.util;

import com.jk.WeatherAPI.dto.SensorDataDTO;
import com.jk.WeatherAPI.dto.StatsQueryRequestDTO;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Validation {

    public final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


    public boolean isValidMetrics(SensorDataDTO sensorDataDTO) {
        return sensorDataDTO.metrics.stream()
                .allMatch(metric -> metric.metricType != null && metric.metricValue != null);
    }

    public boolean isValidSensors(final StatsQueryRequestDTO query){
        if(query.searchAllSensors != null && query.searchAllSensors == true){
            return true;
        } else if(query.sensorIds != null && !query.sensorIds.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isObtainLatestMetrics(final StatsQueryRequestDTO query){
        if(query.startDate == null && query.endDate == null){
            return true;
        }
        return false;
    }

    public Date parseQueryDate(final String dateString, final boolean incrementByOneDay) {
        try {
            Date date = dateFormat.parse(dateString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            if(incrementByOneDay) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            return calendar.getTime();
        } catch (ParseException e) {
            return null;
        }
    }

    public Date getSearchDateForToday(final boolean incrementByOneDay){
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if(incrementByOneDay) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public boolean isValidDates(final StatsQueryRequestDTO query) {
        dateFormat.setLenient(false);

        try {
            if(query.startDate == null || query.endDate == null){
                return false;
            }

            Date startDate = dateFormat.parse(query.startDate);
            Date endDate = dateFormat.parse(query.endDate);

            if (startDate.compareTo(endDate) >= 0) {
                return false;
            }

            Date currentDate = new Date();
            if (endDate.compareTo(currentDate) > 0) {
                return false;
            }

        } catch (ParseException e) {
            return false;
        }
        return true;
    }
}
