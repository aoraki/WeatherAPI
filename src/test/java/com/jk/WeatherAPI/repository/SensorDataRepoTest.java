package com.jk.WeatherAPI.repository;

import com.jk.WeatherAPI.entities.SensorData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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

    //TODO Add lots more tests here!
}
