package com.nhnacademy.sensordata.controller;

import com.nhnacademy.sensordata.entity.humidity.Humidity;
import com.nhnacademy.sensordata.entity.humidity.HumidityMaxMinDaily;
import com.nhnacademy.sensordata.entity.humidity.HumidityMaxMinMonthly;
import com.nhnacademy.sensordata.entity.humidity.HumidityMaxMinWeekly;
import com.nhnacademy.sensordata.service.HumidityService;
import org.junit.jupiter.api.Disabled;
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

@Disabled
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class HumidityRestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private HumidityService humidityService;

    @Test
    void getHumidity() throws Exception {
        Instant time = Instant.now();
        String device = "test device";
        String place = "test place";
        String topic = "test topic";
        Double value = 20.0;
        Humidity humidity = new Humidity(time, device, place, topic, value);

        given(humidityService.getHumidity()).willReturn(humidity);

        mockMvc.perform(get("/api/humidity"))
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
        double maxHumidity = 80.0;
        double minHumidity = 60.0;
        HumidityMaxMinDaily humidityMaxMinDaily = new HumidityMaxMinDaily(time, maxHumidity, minHumidity);
        List<HumidityMaxMinDaily> humidityMaxMinList = Collections.singletonList(humidityMaxMinDaily);

        given(humidityService.getDailyHumidity()).willReturn(humidityMaxMinList);

        mockMvc.perform(get("/api/humidity/day"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].time", equalTo(time.toString())))
                .andExpect(jsonPath("$[0].maxHumidity", equalTo(maxHumidity)))
                .andExpect(jsonPath("$[0].minHumidity", equalTo(minHumidity)))
                .andReturn();
    }

    @Test
    void getWeeklyHumidity() throws Exception {
        Instant time = Instant.now().minus(7, ChronoUnit.DAYS);
        double maxHumidity = 80.0;
        double minHumidity = 60.0;
        HumidityMaxMinWeekly humidityMaxMinDaily = new HumidityMaxMinWeekly(time, maxHumidity, minHumidity);
        List<HumidityMaxMinWeekly> humidityMaxMinList = Collections.singletonList(humidityMaxMinDaily);

        given(humidityService.getWeeklyHumidity()).willReturn(humidityMaxMinList);

        mockMvc.perform(get("/api/humidity/week"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].time", equalTo(time.toString())))
                .andExpect(jsonPath("$[0].maxHumidity", equalTo(maxHumidity)))
                .andExpect(jsonPath("$[0].minHumidity", equalTo(minHumidity)))
                .andReturn();
    }

    @Test
    void getMonthlyHumidity() throws Exception {
        Instant time = Instant.now().minus(30, ChronoUnit.DAYS);
        double maxHumidity = 80.0;
        double minHumidity = 60.0;
        HumidityMaxMinMonthly humidityMaxMinDaily = new HumidityMaxMinMonthly(time, maxHumidity, minHumidity);
        List<HumidityMaxMinMonthly> humidityMaxMinList = Collections.singletonList(humidityMaxMinDaily);

        given(humidityService.getMonthlyHumidity()).willReturn(humidityMaxMinList);

        mockMvc.perform(get("/api/humidity/month"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].time", equalTo(time.toString())))
                .andExpect(jsonPath("$[0].maxHumidity", equalTo(maxHumidity)))
                .andExpect(jsonPath("$[0].minHumidity", equalTo(minHumidity)))
                .andReturn();
    }
}