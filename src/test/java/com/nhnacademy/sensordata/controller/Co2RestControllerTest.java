package com.nhnacademy.sensordata.controller;

import com.nhnacademy.sensordata.exception.Co2NotFoundException;
import com.nhnacademy.sensordata.measurement.co2.Co2;
import com.nhnacademy.sensordata.measurement.co2.Co2MaxMin;
import com.nhnacademy.sensordata.measurement.co2.Co2Mean;
import com.nhnacademy.sensordata.service.Co2Service;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class Co2RestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private Co2Service co2Service;

    @Test
    void getCo2() throws Exception {
        Instant time = Instant.now();
        String device = "test device";
        String place = "test place";
        String topic = "test topic";
        Integer value = 20;
        Co2 co2 = new Co2(time, device, place, topic, value);

        given(co2Service.getCo2(anyString())).willReturn(co2);

        mockMvc.perform(get("/api/sensor/co2")
                        .param("place", place))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.time", equalTo(time.toString())))
                .andExpect(jsonPath("$.device", equalTo(device)))
                .andExpect(jsonPath("$.place", equalTo(place)))
                .andExpect(jsonPath("$.topic", equalTo(topic)))
                .andExpect(jsonPath("$.value", equalTo(value)))
                .andReturn();
    }

    @Test
    void getCo2Exception() throws Exception {
        String message = "Co2를 찾을수 없습니다.";
        String place = "test place";
        given(co2Service.getCo2(anyString()))
                .willThrow(new Co2NotFoundException(message));

        mockMvc.perform(get("/api/sensor/co2")
                        .param("place", place))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", equalTo(message)));
    }

    @Test
    void getDailyCo2() throws Exception {
        Instant time = Instant.now().minus(1, ChronoUnit.DAYS);
        Integer maxCo2 = 80;
        Integer minCo2 = 60;
        String place = "test place";
        Co2MaxMin co2MaxMinDaily = new Co2MaxMin(time, maxCo2, minCo2);
        List<Co2MaxMin> co2MaxMinList = Collections.singletonList(co2MaxMinDaily);

        given(co2Service.getDailyCo2(anyString())).willReturn(co2MaxMinList);

        mockMvc.perform(get("/api/sensor/co2/day")
                        .param("place", place))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].time", equalTo(time.toString())))
                .andExpect(jsonPath("$[0].maxCo2", equalTo(maxCo2)))
                .andExpect(jsonPath("$[0].minCo2", equalTo(minCo2)))
                .andReturn();
    }

    @Test
    void getDailyMeanCo2() throws Exception {
        Instant time = Instant.now().minus(1, ChronoUnit.DAYS);
        Float value = 70.0F;
        String place = "test place";
        Co2Mean co2Mean = new Co2Mean(time, value);
        List<Co2Mean> co2MeanList = Collections.singletonList(co2Mean);

        given(co2Service.getDailyMeanCo2(anyString())).willReturn(co2MeanList);

        mockMvc.perform(get("/api/sensor/co2/day-mean")
                        .param("place", place))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].time", equalTo(time.toString())))
                .andExpect(jsonPath("$[0].value", equalTo(value.doubleValue())));
    }

    @Test
    void getWeeklyCo2() throws Exception {
        Instant time = Instant.now().minus(7, ChronoUnit.DAYS);
        Integer maxCo2 = 80;
        Integer minCo2 = 60;
        String place = "test place";
        Co2MaxMin co2MaxMinDaily = new Co2MaxMin(time, maxCo2, minCo2);
        List<Co2MaxMin> co2MaxMinList = Collections.singletonList(co2MaxMinDaily);

        given(co2Service.getWeeklyCo2(anyString())).willReturn(co2MaxMinList);

        mockMvc.perform(get("/api/sensor/co2/week")
                        .param("place", place))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].time", equalTo(time.toString())))
                .andExpect(jsonPath("$[0].maxCo2", equalTo(maxCo2)))
                .andExpect(jsonPath("$[0].minCo2", equalTo(minCo2)))
                .andReturn();
    }

    @Test
    void getWeeklyCo2Exception() throws Exception {
        String message = "Co2를 찾을수 없습니다.";
        String place = "test place";
        given(co2Service.getWeeklyCo2(anyString()))
                .willThrow(new Co2NotFoundException(message));

        mockMvc.perform(get("/api/sensor/co2/week")
                        .param("place", place))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", equalTo(message)));
    }

    @Test
    void getMonthlyCo2() throws Exception {
        Instant time = Instant.now().minus(30, ChronoUnit.DAYS);
        Integer maxCo2 = 80;
        Integer minCo2 = 60;
        String place = "test place";
        Co2MaxMin co2MaxMinDaily = new Co2MaxMin(time, maxCo2, minCo2);
        List<Co2MaxMin> co2MaxMinList = Collections.singletonList(co2MaxMinDaily);

        given(co2Service.getMonthlyCo2(anyString())).willReturn(co2MaxMinList);

        mockMvc.perform(get("/api/sensor/co2/month")
                        .param("place", place))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].time", equalTo(time.toString())))
                .andExpect(jsonPath("$[0].maxCo2", equalTo(maxCo2)))
                .andExpect(jsonPath("$[0].minCo2", equalTo(minCo2)))
                .andReturn();
    }

    @Test
    void getMonthlyCo2Exception() throws Exception {
        String message = "Co2를 찾을수 없습니다.";
        String place = "test place";
        given(co2Service.getMonthlyCo2(anyString()))
                .willThrow(new Co2NotFoundException(message));

        mockMvc.perform(get("/api/sensor/co2/month")
                        .param("place", place))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", equalTo(message)));
    }
}