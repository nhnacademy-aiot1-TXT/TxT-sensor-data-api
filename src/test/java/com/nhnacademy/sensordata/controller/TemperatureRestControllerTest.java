package com.nhnacademy.sensordata.controller;

import com.nhnacademy.sensordata.exception.TemperatureNotFoundException;
import com.nhnacademy.sensordata.measurement.temperature.Temperature;
import com.nhnacademy.sensordata.measurement.temperature.TemperatureMaxMin;
import com.nhnacademy.sensordata.service.TemperatureService;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        Float value = 20.0f;
        Temperature temperature = new Temperature(time, device, place, topic, value);

        given(temperatureService.getTemperature(anyString()))
                .willReturn(temperature);

        // when
        //then
        mockMvc.perform(get("/api/sensor/temperature")
                        .param("place", place))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.time", equalTo(time.toString())))
                .andExpect(jsonPath("$.device", equalTo(device)))
                .andExpect(jsonPath("$.place", equalTo(place)))
                .andExpect(jsonPath("$.topic", equalTo(topic)))
                .andExpect(jsonPath("$.value", equalTo(value.doubleValue())));
    }

    @Test
    void getTemperatureException() throws Exception {
        String message = "온도를 찾을 수 없습니다.";
        String place = "test place";
        TemperatureNotFoundException exception = new TemperatureNotFoundException(message);
        given(temperatureService.getTemperature(place))
                .willThrow(exception);

        mockMvc.perform(get("/api/sensor/temperature")
                        .param("place", place))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", equalTo(message)));
    }

    @Test
    void getDailyTemperatures() throws Exception {
        // given
        Instant time = Instant.now();
        Float maxTemperature = 24.0f;
        Float minTemperature = 20.0f;
        TemperatureMaxMin temperatureMaxMinDaily = new TemperatureMaxMin(time, maxTemperature, minTemperature);
        List<TemperatureMaxMin> temperatures = List.of(temperatureMaxMinDaily);

        given(temperatureService.getDailyTemperatures(anyString()))
                .willReturn(temperatures);

        // when
        // then
        mockMvc.perform(get("/api/sensor/temperature/day?place=class_a"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].time", equalTo(time.toString())))
                .andExpect(jsonPath("$[0].maxTemperature", equalTo(maxTemperature.doubleValue())))
                .andExpect(jsonPath("$[0].minTemperature", equalTo(minTemperature.doubleValue())));
    }

    @Test
    void getWeeklyTemperatures() throws Exception {
        // given
        Instant time = Instant.now().minus(7, ChronoUnit.DAYS);
        Float maxTemperature = 24.0f;
        Float minTemperature = 20.0f;
        TemperatureMaxMin temperatureMaxMinDaily = new TemperatureMaxMin(time, maxTemperature, minTemperature);
        List<TemperatureMaxMin> temperatures = List.of(temperatureMaxMinDaily);

        given(temperatureService.getWeeklyTemperatures())
                .willReturn(temperatures);

        // when
        // then
        mockMvc.perform(get("/api/sensor/temperature/week"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].time", equalTo(time.toString())))
                .andExpect(jsonPath("$[0].maxTemperature", equalTo(maxTemperature.doubleValue())))
                .andExpect(jsonPath("$[0].minTemperature", equalTo(minTemperature.doubleValue())));
    }

    @Test
    void getWeeklyTemperaturesException() throws Exception {
        String message = "온도를 찾을 수 없습니다.";
        TemperatureNotFoundException exception = new TemperatureNotFoundException(message);
        given(temperatureService.getWeeklyTemperatures())
                .willThrow(exception);

        mockMvc.perform(get("/api/sensor/temperature/week"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", equalTo(message)));
    }

    @Test
    void getMonthlyTemperatures() throws Exception {
        // given
        Instant time = Instant.now().minus(30, ChronoUnit.DAYS);
        Float maxTemperature = 24.0f;
        Float minTemperature = 20.0f;
        String place = "test place";
        TemperatureMaxMin temperatureMaxMinDaily = new TemperatureMaxMin(time, maxTemperature, minTemperature);
        List<TemperatureMaxMin> temperatures = List.of(temperatureMaxMinDaily);

        given(temperatureService.getMonthlyTemperatures(anyString()))
                .willReturn(temperatures);

        // when
        // then
        mockMvc.perform(get("/api/sensor/temperature/month")
                        .param("place", place))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].time", equalTo(time.toString())))
                .andExpect(jsonPath("$[0].maxTemperature", equalTo(maxTemperature.doubleValue())))
                .andExpect(jsonPath("$[0].minTemperature", equalTo(minTemperature.doubleValue())));
    }

    @Test
    void getMonthlyTemperaturesException() throws Exception {
        String message = "온도를 찾을 수 없습니다.";
        String place = "test place";
        TemperatureNotFoundException exception = new TemperatureNotFoundException(message);
        given(temperatureService.getMonthlyTemperatures(anyString()))
                .willThrow(exception);

        mockMvc.perform(get("/api/sensor/temperature/month")
                        .param("place", place))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", equalTo(message)));
    }
}