package com.nhnacademy.sensordata.controller;

import com.nhnacademy.sensordata.exception.HumidityNotFoundException;
import com.nhnacademy.sensordata.measurement.humidity.Humidity;
import com.nhnacademy.sensordata.measurement.humidity.HumidityMaxMin;
import com.nhnacademy.sensordata.measurement.humidity.HumidityMean;
import com.nhnacademy.sensordata.service.HumidityService;
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
        Float value = 20.0f;
        Humidity humidity = new Humidity(time, device, place, topic, value);

        given(humidityService.getHumidity(anyString())).willReturn(humidity);

        mockMvc.perform(get("/api/sensor/humidity")
                        .param("place", place))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.time", equalTo(time.toString())))
                .andExpect(jsonPath("$.device", equalTo(device)))
                .andExpect(jsonPath("$.place", equalTo(place)))
                .andExpect(jsonPath("$.topic", equalTo(topic)))
                .andExpect(jsonPath("$.value", equalTo(value.doubleValue())))
                .andReturn();
    }

    @Test
    void getHumidityException() throws Exception {
        String message = "습도를 찾을수 없습니다.";
        String place = "test place";

        given(humidityService.getHumidity(anyString()))
                .willThrow(new HumidityNotFoundException(message));

        mockMvc.perform(get("/api/sensor/humidity")
                        .param("place", place))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", equalTo(message)));
    }

    @Test
    void getDailyHumidity() throws Exception {
        Instant time = Instant.now().minus(1, ChronoUnit.DAYS);
        Float maxHumidity = 80.0f;
        Float minHumidity = 60.0f;
        String place = "test place";
        HumidityMaxMin humidityMaxMinDaily = new HumidityMaxMin(time, maxHumidity, minHumidity);
        List<HumidityMaxMin> humidityMaxMinList = Collections.singletonList(humidityMaxMinDaily);

        given(humidityService.getDailyHumidity(anyString())).willReturn(humidityMaxMinList);

        mockMvc.perform(get("/api/sensor/humidity/day")
                        .param("place", place))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].time", equalTo(time.toString())))
                .andExpect(jsonPath("$[0].maxHumidity", equalTo(maxHumidity.doubleValue())))
                .andExpect(jsonPath("$[0].minHumidity", equalTo(minHumidity.doubleValue())))
                .andReturn();
    }

    @Test
    void getDailyMeanHumidity() throws Exception {
        Instant time = Instant.now().minus(1, ChronoUnit.DAYS);
        Float value = 70.0F;
        String place = "test place";
        HumidityMean humidityMean = new HumidityMean(time, value);
        List<HumidityMean> humidityMeanList = Collections.singletonList(humidityMean);

        given(humidityService.getDailyMeanHumidity(anyString())).willReturn(humidityMeanList);

        mockMvc.perform(get("/api/sensor/humidity/day-mean")
                        .param("place", place))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].time", equalTo(time.toString())))
                .andExpect(jsonPath("$[0].value", equalTo(value.doubleValue())));
    }

    @Test
    void getWeeklyHumidity() throws Exception {
        Instant time = Instant.now().minus(7, ChronoUnit.DAYS);
        Float maxHumidity = 80.0f;
        Float minHumidity = 60.0f;
        String place = "test place";
        HumidityMaxMin humidityMaxMinDaily = new HumidityMaxMin(time, maxHumidity, minHumidity);
        List<HumidityMaxMin> humidityMaxMinList = Collections.singletonList(humidityMaxMinDaily);

        given(humidityService.getWeeklyHumidity(anyString())).willReturn(humidityMaxMinList);

        mockMvc.perform(get("/api/sensor/humidity/week")
                        .param("place", place))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].time", equalTo(time.toString())))
                .andExpect(jsonPath("$[0].maxHumidity", equalTo(maxHumidity.doubleValue())))
                .andExpect(jsonPath("$[0].minHumidity", equalTo(minHumidity.doubleValue())))
                .andReturn();
    }

    @Test
    void getWeeklyHumidityException() throws Exception {
        String message = "습도를 찾을수 없습니다.";
        String place = "test place";

        given(humidityService.getWeeklyHumidity(anyString()))
                .willThrow(new HumidityNotFoundException(message));

        mockMvc.perform(get("/api/sensor/humidity/week")
                        .param("place", place))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", equalTo(message)));
    }

    @Test
    void getMonthlyHumidity() throws Exception {
        Instant time = Instant.now().minus(30, ChronoUnit.DAYS);
        Float maxHumidity = 80.0f;
        Float minHumidity = 60.0f;
        String place = "test place";
        HumidityMaxMin humidityMaxMinDaily = new HumidityMaxMin(time, maxHumidity, minHumidity);
        List<HumidityMaxMin> humidityMaxMinList = Collections.singletonList(humidityMaxMinDaily);

        given(humidityService.getMonthlyHumidity(anyString())).willReturn(humidityMaxMinList);

        mockMvc.perform(get("/api/sensor/humidity/month")
                        .param("place", place))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].time", equalTo(time.toString())))
                .andExpect(jsonPath("$[0].maxHumidity", equalTo(maxHumidity.doubleValue())))
                .andExpect(jsonPath("$[0].minHumidity", equalTo(minHumidity.doubleValue())))
                .andReturn();
    }

    @Test
    void getMonthlyHumidityException() throws Exception {
        String message = "습도를 찾을수 없습니다.";
        String place = "test place";
        given(humidityService.getMonthlyHumidity(anyString()))
                .willThrow(new HumidityNotFoundException(message));

        mockMvc.perform(get("/api/sensor/humidity/month")
                        .param("place", place))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", equalTo(message)));
    }
}