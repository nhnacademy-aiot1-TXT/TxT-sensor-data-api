package com.nhnacademy.sensordata.controller;

import com.nhnacademy.sensordata.entity.co2.Co2;
import com.nhnacademy.sensordata.entity.co2.Co2MaxMinDaily;
import com.nhnacademy.sensordata.entity.co2.Co2MaxMinMonthly;
import com.nhnacademy.sensordata.entity.co2.Co2MaxMinWeekly;
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
    void getHumidity() throws Exception {
        Instant time = Instant.now();
        String device = "test device";
        String place = "test place";
        String topic = "test topic";
        Integer value = 20;
        Co2 co2 = new Co2(time, device, place, topic, value);

        given(co2Service.getCo2()).willReturn(co2);

        mockMvc.perform(get("/api/co2"))
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
    void getDailyHumidity() throws Exception {
        Instant time = Instant.now().minus(1, ChronoUnit.DAYS);
        Integer maxCo2 = 80;
        Integer minCo2 = 60;
        Co2MaxMinDaily co2MaxMinDaily = new Co2MaxMinDaily(time, maxCo2, minCo2);
        List<Co2MaxMinDaily> co2MaxMinList = Collections.singletonList(co2MaxMinDaily);

        given(co2Service.getDailyCo2()).willReturn(co2MaxMinList);

        mockMvc.perform(get("/api/co2/day"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].time", equalTo(time.toString())))
                .andExpect(jsonPath("$[0].maxCo2", equalTo(maxCo2)))
                .andExpect(jsonPath("$[0].minCo2", equalTo(minCo2)))
                .andReturn();
    }

    @Test
    void getWeeklyHumidity() throws Exception {
        Instant time = Instant.now().minus(7, ChronoUnit.DAYS);
        Integer maxCo2 = 80;
        Integer minCo2 = 60;
        Co2MaxMinWeekly co2MaxMinDaily = new Co2MaxMinWeekly(time, maxCo2, minCo2);
        List<Co2MaxMinWeekly> co2MaxMinList = Collections.singletonList(co2MaxMinDaily);

        given(co2Service.getWeeklyCo2()).willReturn(co2MaxMinList);

        mockMvc.perform(get("/api/co2/week"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].time", equalTo(time.toString())))
                .andExpect(jsonPath("$[0].maxCo2", equalTo(maxCo2)))
                .andExpect(jsonPath("$[0].minCo2", equalTo(minCo2)))
                .andReturn();
    }

    @Test
    void getMonthlyHumidity() throws Exception {
        Instant time = Instant.now().minus(30, ChronoUnit.DAYS);
        Integer maxCo2 = 80;
        Integer minCo2 = 60;
        Co2MaxMinMonthly co2MaxMinDaily = new Co2MaxMinMonthly(time, maxCo2, minCo2);
        List<Co2MaxMinMonthly> co2MaxMinList = Collections.singletonList(co2MaxMinDaily);

        given(co2Service.getMonthlyCo2()).willReturn(co2MaxMinList);

        mockMvc.perform(get("/api/co2/month"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].time", equalTo(time.toString())))
                .andExpect(jsonPath("$[0].maxCo2", equalTo(maxCo2)))
                .andExpect(jsonPath("$[0].minCo2", equalTo(minCo2)))
                .andReturn();
    }
}