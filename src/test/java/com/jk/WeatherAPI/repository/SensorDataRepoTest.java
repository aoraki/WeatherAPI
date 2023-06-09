package com.jk.WeatherAPI.repository;

import com.jk.WeatherAPI.entities.SensorData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class SensorDataRepoTest {

    @Autowired
    private SensorDataRepo sensorDataRepo;

    @Test
    public void testFindBySampleId() {
        SensorData sensorData = new SensorData();
        sensorData.setSampleId(111L);
        sensorDataRepo.save(sensorData);

        Optional<SensorData> result = sensorDataRepo.findBySampleId(111L);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(111L, result.get().getSampleId());
    }

    @Test
    public void testFindBySampleIdNotFound() {
        Optional<SensorData> result = sensorDataRepo.findBySampleId(456L);
        Assertions.assertTrue(result.isEmpty());
    }

    /*
    @Test
    public void testFindAll(){
        SensorData sensorData1 = new SensorData();
        sensorData1.setSampleId(111L);

        SensorData sensorData2 = new SensorData();
        sensorData1.setSampleId(222L);

        sensorDataRepo.save(sensorData1);
        sensorDataRepo.save(sensorData2);

        List<SensorData> result = sensorDataRepo.findAll();
        Assertions.assertTrue(result.size() == 2);
    }*/

    /*
    @Test
    public void testGetAverageTemperatureBySensorIdsAndDateRange() {
        // Create sample SensorData entities and save them
        SensorData sensorData1 = new SensorData();
        sensorData1.setSampleId(111L);
        sensorData1.setSensorId(1L);
        sensorData1.setTemperature(25.5);
        sensorData1.setSampleTime(new Timestamp(new Date().getTime()));
        sensorDataRepo.save(sensorData1);

        SensorData sensorData2 = new SensorData();
        sensorData2.setSampleId(222L);
        sensorData2.setSensorId(1L);
        sensorData2.setTemperature(30.0);
        sensorData2.setSampleTime(new Timestamp(new Date().getTime()));
        sensorDataRepo.save(sensorData2);

        // Call the getAverageTemperatureBySensorIdsAndDateRange method
        List<Long> sensorIds = Arrays.asList(1L);
        Date startDate = new Date();
        Date endDate = new Date();
        Double averageTemperature = sensorDataRepo.getAverageTemperatureBySensorIdsAndDateRange(sensorIds, startDate, endDate);

        // Assert that the average temperature is calculated correctly
        Assertions.assertEquals(27.75, averageTemperature);
    }*/
}
