package com.nhnacademy.sensordata.controller;

import com.nhnacademy.sensordata.entity.temperature.Temperature;
import com.nhnacademy.sensordata.entity.temperature.TemperatureMaxMinDaily;
import com.nhnacademy.sensordata.entity.temperature.TemperatureMaxMinMonthly;
import com.nhnacademy.sensordata.entity.temperature.TemperatureMaxMinWeekly;
import com.nhnacademy.sensordata.service.TemperatureService;
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
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Disabled
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class TemperatureRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TemperatureService temperatureService;

    @Test
    void getTemperature() throws Exception {
        // given
        Instant time = Instant.now();
        String device = "test device";
        String place = "test place";
        String topic = "test topic";
        Double value = 20.0;
        Temperature temperature = new Temperature(time, device, place, topic, value);

        given(temperatureService.getTemperature())
                .willReturn(temperature);

        // when
        //then
        mockMvc.perform(get("/api/temperature"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.time", equalTo(time.toString())))
                .andExpect(jsonPath("$.device", equalTo(device)))
                .andExpect(jsonPath("$.place", equalTo(place)))
                .andExpect(jsonPath("$.topic", equalTo(topic)))
                .andExpect(jsonPath("$.value", equalTo(value)));
    }

    @Test
    void getDailyTemperatures() throws Exception {
        // given
        Instant time = Instant.now();
        Double maxTemperature = 24.0;
        Double minTemperature = 20.0;
        TemperatureMaxMinDaily temperatureMaxMinDaily = new TemperatureMaxMinDaily(time, maxTemperature, minTemperature);
        List<TemperatureMaxMinDaily> temperatures = List.of(temperatureMaxMinDaily);

        given(temperatureService.getDailyTemperatures())
                .willReturn(temperatures);

        // when
        // then
        mockMvc.perform(get("/api/temperature/day"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].time", equalTo(time.toString())))
                .andExpect(jsonPath("$[0].maxTemperature", equalTo(maxTemperature)))
                .andExpect(jsonPath("$[0].minTemperature", equalTo(minTemperature)));
    }

    @Test
    void getWeeklyTemperatures() throws Exception {
        // given
        Instant time = Instant.now().minus(7, ChronoUnit.DAYS);
        Double maxTemperature = 24.0;
        Double minTemperature = 20.0;
        TemperatureMaxMinWeekly temperatureMaxMinDaily = new TemperatureMaxMinWeekly(time, maxTemperature, minTemperature);
        List<TemperatureMaxMinWeekly> temperatures = List.of(temperatureMaxMinDaily);

        given(temperatureService.getWeeklyTemperatures())
                .willReturn(temperatures);

        // when
        // then
        mockMvc.perform(get("/api/temperature/week"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].time", equalTo(time.toString())))
                .andExpect(jsonPath("$[0].maxTemperature", equalTo(maxTemperature)))
                .andExpect(jsonPath("$[0].minTemperature", equalTo(minTemperature)));
    }

    @Test
    void getMonthlyTemperatures() throws Exception {
        // given
        Instant time = Instant.now().minus(30, ChronoUnit.DAYS);
        Double maxTemperature = 24.0;
        Double minTemperature = 20.0;
        TemperatureMaxMinMonthly temperatureMaxMinDaily = new TemperatureMaxMinMonthly(time, maxTemperature, minTemperature);
        List<TemperatureMaxMinMonthly> temperatures = List.of(temperatureMaxMinDaily);

        given(temperatureService.getMonthlyTemperatures())
                .willReturn(temperatures);

        // when
        // then
        mockMvc.perform(get("/api/temperature/month"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].time", equalTo(time.toString())))
                .andExpect(jsonPath("$[0].maxTemperature", equalTo(maxTemperature)))
                .andExpect(jsonPath("$[0].minTemperature", equalTo(minTemperature)));
    }
}