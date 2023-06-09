package com.jk.WeatherAPI.controller;

import com.jk.WeatherAPI.exception.AppException;
import com.jk.WeatherAPI.service.MetricsService;
import com.jk.WeatherAPI.util.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(WeatherController.class)
public class WeatherControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MetricsService metricsService;

    final TestUtils testUtils = new TestUtils();

    @Test
    void getStatsInvalidHTTPMethod() throws Exception {
        mockMvc.perform(put("/v1/weather/stats"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void getAllSamplesInvalidHTTPMethod() throws Exception {
        mockMvc.perform(put("/v1/weather/metrics"))
            .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void getAllSamplesEmptyList200Response() throws Exception {
        given(metricsService.getAllSamples()).willReturn(new ArrayList<>());

        mockMvc.perform(get("/v1/weather/metrics").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getAllSamples200ResponseOneSample() throws Exception {
        given(metricsService.getAllSamples()).willReturn(Collections.singletonList(testUtils.generateSensorDataDTO(1L, 2L)));

        mockMvc.perform(get("/v1/weather/metrics").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$.[0].sampleId").value(1))
            .andExpect(jsonPath("$.[0].sensorId").value(2))
            .andExpect(jsonPath("$.[0].metrics").isArray())
            .andExpect(jsonPath("$.[0].metrics.[0].metricType").value("TEMPERATURE"))
            .andExpect(jsonPath("$.[0].metrics.length()").value(2));
    }

    @Test
    void getAllSamples200ResponseMultipleSamples() throws Exception {
        given(metricsService.getAllSamples()).willReturn(Arrays.asList(testUtils.generateSensorDataDTO(11L, 111L), testUtils.generateSensorDataDTO(22L, 222L)));

        mockMvc.perform(get("/v1/weather/metrics").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$.[0].sampleId").value(11))
            .andExpect(jsonPath("$.[0].sensorId").value(111))
            .andExpect(jsonPath("$.[0].metrics").isArray())
            .andExpect(jsonPath("$.[0].metrics.[0].metricType").value("TEMPERATURE"))
            .andExpect(jsonPath("$.[0].metrics.[0].metricValue").value(10.0))
            .andExpect(jsonPath("$.[0].metrics.length()").value(2))
            .andExpect(jsonPath("$.[1].sampleId").value(22))
            .andExpect(jsonPath("$.[1].sensorId").value(222))
            .andExpect(jsonPath("$.[1].metrics").isArray())
            .andExpect(jsonPath("$.[1].metrics.[1].metricType").value("HUMIDITY"))
            .andExpect(jsonPath("$.[1].metrics.[1].metricValue").value(20.0))
            .andExpect(jsonPath("$.[1].metrics.length()").value(2));
    }

    @Test
    void getAllSamples406Response() throws Exception {
        given(metricsService.getAllSamples()).willReturn(new ArrayList<>());
        mockMvc.perform(get("/v1/weather/metrics").accept(MediaType.APPLICATION_XML_VALUE))
            .andExpect(status().isNotAcceptable());
    }

    @Test
    void getSamplesBySampleIdWrongMethod405Response() throws Exception {
        given(metricsService.getAllSamples()).willReturn(new ArrayList<>());
        mockMvc.perform(post("/v1/weather/metrics/{sampleId}", 111L))
            .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void getSamplesBySampleIdSuccessfulResponse() throws Exception {
        given(metricsService.getSampleBySampleId(1L)).willReturn(testUtils.generateSensorDataDTO(1L, 2L));
        mockMvc.perform(get("/v1/weather/metrics/{sampleId}", 1L))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.sampleId").value(1))
            .andExpect(jsonPath("$.sensorId").value(2))
            .andExpect(jsonPath("$.metrics").isArray())
            .andExpect(jsonPath("$.metrics.[0].metricType").value("TEMPERATURE"))
            .andExpect(jsonPath("$.metrics.length()").value(2));
    }

    @Test
    void getSamplesBySampleIdNotFound() throws Exception {
        given(metricsService.getSampleBySampleId(1L)).willThrow(new AppException(404, "Cannot find Sample with sample Id 1"));
        mockMvc.perform(get("/v1/weather/metrics/{sampleId}", 1L))
            .andExpect(status().isNotFound());
    }

    @Test
    void createSampleNoSampleIdBadRequest() throws Exception {
        final String jsonPayload = "{\"sensorId\" : 1, \"metrics\" : [{\"metricType\" : \"TEMP\", \"metricValue\" : 10.0}, {\"metricType\" : \"HUM\", \"metricValue\" : 20.0}]}";
        mockMvc.perform(post("/v1/weather/metrics")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
            .andExpect(status().isBadRequest());
    }

    @Test
    void createSampleNullSampleIdBadRequest() throws Exception {
        final String jsonPayload = "{\"sampleId\" : null, \"sensorId\" : 1, \"metrics\" : [{\"metricType\" : \"TEMP\", \"metricValue\" : 10.0}, {\"metricType\" : \"HUM\", \"metricValue\" : 20.0}]}";
        mockMvc.perform(post("/v1/weather/metrics")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
            .andExpect(status().isBadRequest());
    }

    @Test
    void createSampleNoSensorIdBadRequest() throws Exception {
        final String jsonPayload = "{\"sampleId\" : 1, \"metrics\" : [{\"metricType\" : \"TEMP\", \"metricValue\" : 10.0}, {\"metricType\" : \"HUM\", \"metricValue\" : 20.0}]}";
        mockMvc.perform(post("/v1/weather/metrics")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
            .andExpect(status().isBadRequest());
    }

    @Test
    void createSampleNullSensorIdBadRequest() throws Exception {
        final String jsonPayload = "{\"sampleId\" : 1, \"sensorId\" : null, \"metrics\" : [{\"metricType\" : \"TEMP\", \"metricValue\" : 10.0}, {\"metricType\" : \"HUM\", \"metricValue\" : 20.0}]}";
        mockMvc.perform(post("/v1/weather/metrics")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
            .andExpect(status().isBadRequest());
    }

    @Test
    void createSampleNoMetricsBadRequest() throws Exception {
        final String jsonPayload = "{\"sampleId\" : 1, \"sensorId\" : 1}";
        mockMvc.perform(post("/v1/weather/metrics")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
            .andExpect(status().isBadRequest());
    }

    @Test
    void createSampleEmptyMetricsBadRequest() throws Exception {
        final String jsonPayload = "{\"sampleId\" : 1, \"sensorId\" : 1, \"metrics\" : []}";
        mockMvc.perform(post("/v1/weather/metrics")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
            .andExpect(status().isBadRequest());
    }

    @Test
    void createSampleSampleIdNullValueBadRequest() throws Exception {
        final String jsonPayload = "{\"sampleId\" : null, \"sensorId\" : 1, \"metrics\" : [{\"metricType\" : \"TEMP\", \"metricValue\" : 10.0}, {\"metricType\" : \"HUM\", \"metricValue\" : 20.0}]}";
        mockMvc.perform(post("/v1/weather/metrics")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
            .andExpect(status().isBadRequest());
    }

    @Test
    void createSampleSensorIdNullValueBadRequest() throws Exception {
        final String jsonPayload = "{\"sampleId\" : 1, \"sensorId\" : null, \"metrics\" : [{\"metricType\" : \"TEMP\", \"metricValue\" : 10.0}, {\"metricType\" : \"HUM\", \"metricValue\" : 20.0}]}";
        mockMvc.perform(post("/v1/weather/metrics")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
            .andExpect(status().isBadRequest());
    }

    @Test
    void createSampleMetricsNullValueBadRequest() throws Exception {
        final String jsonPayload = "{\"sampleId\" : 1, \"sensorId\" : 1, \"metrics\" : null}";
        mockMvc.perform(post("/v1/weather/metrics")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
            .andExpect(status().isBadRequest());
    }

    @Test
    void createSampleValidJSonButGarbageBadRequest() throws Exception {
        final String jsonPayload = "{\"random\" : true}";
        mockMvc.perform(post("/v1/weather/metrics")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
            .andExpect(status().isBadRequest());
    }

    @Test
    void createSampleGarbageInputBadRequest() throws Exception {
        final String jsonPayload = "34634CVBFGHFGHFG";
        mockMvc.perform(post("/v1/weather/metrics")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
            .andExpect(status().isBadRequest());
    }

    @Test
    void createSampleSuccess201Response() throws Exception {
        given(metricsService.createSample(any())).willReturn(testUtils.generateSensorDataDTO(1L, 1L));

        final String jsonPayload = "{\"sampleId\" : 1, \"sensorId\" : 1, \"metrics\" : [{\"metricType\" : \"TEMP\", \"metricValue\" : 10.0}, {\"metricType\" : \"HUM\", \"metricValue\" : 20.0}]}";

        mockMvc.perform(post("/v1/weather/metrics")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.sampleId").value(1))
            .andExpect(jsonPath("$.sensorId").value(1))
            .andExpect(jsonPath("$.metrics").isArray())
            .andExpect(jsonPath("$.metrics.[0].metricType").value("TEMPERATURE"))
            .andExpect(jsonPath("$.metrics.length()").value(2));
    }

    @Test
    void createSample409Conflict() throws Exception {
        given(metricsService.createSample(any())).willThrow(new AppException(409, "A sample with sampleId 1 already exists"));
        final String jsonPayload = "{\"sampleId\" : 1, \"sensorId\" : 1, \"metrics\" : [{\"metricType\" : \"TEMP\", \"metricValue\" : 10.0}, {\"metricType\" : \"HUM\", \"metricValue\" : 20.0}]}";

        mockMvc.perform(post("/v1/weather/metrics")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
            .andExpect(status().isConflict());
    }

    @Test
    void createSampleReturn415() throws Exception {
        final String jsonPayload = "{\"sampleId\" : 1, \"sensorId\" : 1, \"metrics\" : [{\"metricType\" : \"TEMP\", \"metricValue\" : 10.0}, {\"metricType\" : \"HUM\", \"metricValue\" : 20.0}]}";

        mockMvc.perform(post("/v1/weather/metrics")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_XML)
                .content(jsonPayload))
            .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void updateSampleNoSampleIdBadRequest() throws Exception {
        final String jsonPayload = "{\"sensorId\" : 1, \"metrics\" : [{\"metricType\" : \"TEMP\", \"metricValue\" : 10.0}, {\"metricType\" : \"HUM\", \"metricValue\" : 20.0}]}";
        mockMvc.perform(patch("/v1/weather/metrics")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
            .andExpect(status().isBadRequest());
    }

    @Test
    void updateSampleNullSampleIdBadRequest() throws Exception {
        final String jsonPayload = "{\"sampleId\" : null, \"sensorId\" : 1, \"metrics\" : [{\"metricType\" : \"TEMP\", \"metricValue\" : 10.0}, {\"metricType\" : \"HUM\", \"metricValue\" : 20.0}]}";
        mockMvc.perform(patch("/v1/weather/metrics")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
            .andExpect(status().isBadRequest());
    }

    @Test
    void updateSampleNoSensorIdBadRequest() throws Exception {
        final String jsonPayload = "{\"sampleId\" : 1, \"metrics\" : [{\"metricType\" : \"TEMP\", \"metricValue\" : 10.0}, {\"metricType\" : \"HUM\", \"metricValue\" : 20.0}]}";
        mockMvc.perform(patch("/v1/weather/metrics")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
            .andExpect(status().isBadRequest());
    }

    @Test
    void updateSampleNullSensorIdBadRequest() throws Exception {
        final String jsonPayload = "{\"sampleId\" : 1, \"sensorId\" : null, \"metrics\" : [{\"metricType\" : \"TEMP\", \"metricValue\" : 10.0}, {\"metricType\" : \"HUM\", \"metricValue\" : 20.0}]}";
        mockMvc.perform(patch("/v1/weather/metrics")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
            .andExpect(status().isBadRequest());
    }

    @Test
    void updateSampleNoMetricsBadRequest() throws Exception {
        final String jsonPayload = "{\"sampleId\" : 1, \"sensorId\" : 1}";
        mockMvc.perform(patch("/v1/weather/metrics")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
            .andExpect(status().isBadRequest());
    }

    @Test
    void updateSampleEmptyMetricsBadRequest() throws Exception {
        final String jsonPayload = "{\"sampleId\" : 1, \"sensorId\" : 1, \"metrics\" : []}";
        mockMvc.perform(patch("/v1/weather/metrics")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
            .andExpect(status().isBadRequest());
    }

    @Test
    void updateSampleSampleIdNullValueBadRequest() throws Exception {
        final String jsonPayload = "{\"sampleId\" : null, \"sensorId\" : 1, \"metrics\" : [{\"metricType\" : \"TEMP\", \"metricValue\" : 10.0}, {\"metricType\" : \"HUM\", \"metricValue\" : 20.0}]}";
        mockMvc.perform(patch("/v1/weather/metrics")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
            .andExpect(status().isBadRequest());
    }

    @Test
    void updateSampleSensorIdNullValueBadRequest() throws Exception {
        final String jsonPayload = "{\"sampleId\" : 1, \"sensorId\" : null, \"metrics\" : [{\"metricType\" : \"TEMP\", \"metricValue\" : 10.0}, {\"metricType\" : \"HUM\", \"metricValue\" : 20.0}]}";
        mockMvc.perform(patch("/v1/weather/metrics")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
            .andExpect(status().isBadRequest());
    }

    @Test
    void updateSampleMetricsNullValueBadRequest() throws Exception {
        final String jsonPayload = "{\"sampleId\" : 1, \"sensorId\" : 1, \"metrics\" : null}";
        mockMvc.perform(patch("/v1/weather/metrics")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
            .andExpect(status().isBadRequest());
    }

    @Test
    void updateSampleValidJSonButGarbageBadRequest() throws Exception {
        final String jsonPayload = "{\"random\" : true}";
        mockMvc.perform(patch("/v1/weather/metrics")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
            .andExpect(status().isBadRequest());
    }

    @Test
    void updateSampleGarbageInputBadRequest() throws Exception {
        final String jsonPayload = "34634CVBFGHFGHFG";
        mockMvc.perform(patch("/v1/weather/metrics")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
            .andExpect(status().isBadRequest());
    }

    @Test
    void updateSampleSuccess200Response() throws Exception {
        given(metricsService.updateSample(any())).willReturn(testUtils.generateSensorDataDTO(1L, 111L));
        final String jsonPayload = "{\"sampleId\" : 1, \"sensorId\" : 111, \"metrics\" : [{\"metricType\" : \"TEMP\", \"metricValue\" : 10.0}, {\"metricType\" : \"HUM\", \"metricValue\" : 20.0}]}";

        mockMvc.perform(patch("/v1/weather/metrics")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.sampleId").value(1))
            .andExpect(jsonPath("$.sensorId").value(111))
            .andExpect(jsonPath("$.metrics").isArray())
            .andExpect(jsonPath("$.metrics.[0].metricType").value("TEMPERATURE"))
            .andExpect(jsonPath("$.metrics.length()").value(2));
    }

    @Test
    void updateSampleNotFoundResponse() throws Exception {
        given(metricsService.updateSample(any())).willThrow(new AppException(404, "A sample with sample id 1 does not exist. Cannot update"));
        final String jsonPayload = "{\"sampleId\" : 1, \"sensorId\" : 1, \"metrics\" : [{\"metricType\" : \"TEMP\", \"metricValue\" : 10.0}, {\"metricType\" : \"HUM\", \"metricValue\" : 20.0}]}";

        mockMvc.perform(patch("/v1/weather/metrics")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
            .andExpect(status().isNotFound());
    }

    @Test
    void updateSampleReturn415() throws Exception {
        final String jsonPayload = "{\"sampleId\" : 1, \"sensorId\" : 1, \"metrics\" : [{\"metricType\" : \"TEMP\", \"metricValue\" : 10.0}, {\"metricType\" : \"HUM\", \"metricValue\" : 20.0}]}";
        mockMvc.perform(patch("/v1/weather/metrics")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_XML)
                .content(jsonPayload))
            .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void deleteSampleSuccess() throws Exception {
        given(metricsService.deleteSample(123L)).willReturn(true);
        mockMvc.perform(delete("/v1/weather/metrics/{sampleId}", 123L))
            .andExpect(status().isOk());
    }

    @Test
    void deleteSampleNotFoundResponse() throws Exception {
        given(metricsService.deleteSample(123L)).willThrow(new AppException(404, "Cannot find Sample with sample Id 123"));
        mockMvc.perform(delete("/v1/weather/metrics/{sampleId}", 123L))
            .andExpect(status().isNotFound());
    }
}