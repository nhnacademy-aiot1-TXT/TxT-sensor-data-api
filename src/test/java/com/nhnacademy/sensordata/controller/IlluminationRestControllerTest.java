package com.nhnacademy.sensordata.controller;

import com.nhnacademy.sensordata.exception.IlluminationNotFoundException;
import com.nhnacademy.sensordata.measurement.illumination.Illumination;
import com.nhnacademy.sensordata.measurement.illumination.IlluminationMaxMin;
import com.nhnacademy.sensordata.measurement.illumination.IlluminationMean;
import com.nhnacademy.sensordata.service.IlluminationService;
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
class IlluminationRestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private IlluminationService illuminationService;

    @Test
    void getIllumination() throws Exception {
        // given
        Instant time = Instant.now();
        String device = "test device";
        String place = "test place";
        String topic = "test topic";
        Integer value = 65;
        Illumination illumination = new Illumination(time, device, place, topic, value);

        // when
        given(illuminationService.getIllumination(anyString()))
                .willReturn(illumination);

        //then
        mockMvc.perform(get("/api/sensor/illumination")
                        .param("place", place))
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
    void getIlluminationException() throws Exception {
        String message = "조도를 찾을 수 없습니다.";
        String place = "test place";
        IlluminationNotFoundException exception = new IlluminationNotFoundException(message);

        given(illuminationService.getIllumination(anyString()))
                .willThrow(exception);

        mockMvc.perform(get("/api/sensor/illumination")
                        .param("place", place))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", equalTo(message)));
    }

    @Test
    void getDailyIlluminations() throws Exception {
        // given
        Instant time = Instant.now();
        Integer maxIllumination = 100;
        Integer minIllumination = 50;
        String place = "test place";
        IlluminationMaxMin illuminationMaxMinDaily = new IlluminationMaxMin(time, maxIllumination, minIllumination);
        List<IlluminationMaxMin> illuminations = List.of(illuminationMaxMinDaily);

        // when
        given(illuminationService.getDailyIlluminations(anyString()))
                .willReturn(illuminations);

        //then
        mockMvc.perform(get("/api/sensor/illumination/day")
                        .param("place", place))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].time", equalTo(time.toString())))
                .andExpect(jsonPath("$[0].maxIllumination", equalTo(maxIllumination)))
                .andExpect(jsonPath("$[0].minIllumination", equalTo(minIllumination)));
    }

    @Test
    void getDailyMeanIllumination() throws Exception {
        Instant time = Instant.now().minus(1, ChronoUnit.DAYS);
        Float value = 70.0F;
        String place = "test place";
        IlluminationMean illuminationMean = new IlluminationMean(time, value);
        List<IlluminationMean> illuminationMeanList = Collections.singletonList(illuminationMean);

        given(illuminationService.getDailyIlluminationsMean(anyString())).willReturn(illuminationMeanList);

        mockMvc.perform(get("/api/sensor/illumination/day-mean")
                        .param("place", place))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].time", equalTo(time.toString())))
                .andExpect(jsonPath("$[0].value", equalTo(value.doubleValue())));
    }

    @Test
    void getWeeklyIlluminations() throws Exception {
        // given
        Instant time = Instant.now().minus(7, ChronoUnit.DAYS);
        Integer maxIllumination = 100;
        Integer minIllumination = 50;
        String place = "test place";
        IlluminationMaxMin illuminationMaxMinDaily = new IlluminationMaxMin(time, maxIllumination, minIllumination);
        List<IlluminationMaxMin> illuminations = List.of(illuminationMaxMinDaily);

        // when
        given(illuminationService.getWeeklyIlluminations(anyString()))
                .willReturn(illuminations);

        //then
        mockMvc.perform(get("/api/sensor/illumination/week")
                        .param("place", place))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].time", equalTo(time.toString())))
                .andExpect(jsonPath("$[0].maxIllumination", equalTo(maxIllumination)))
                .andExpect(jsonPath("$[0].minIllumination", equalTo(minIllumination)));
    }

    @Test
    void getWeeklyIlluminationsException() throws Exception {
        String message = "조도를 찾을 수 없습니다.";
        String place = "test place";
        IlluminationNotFoundException exception = new IlluminationNotFoundException(message);

        given(illuminationService.getWeeklyIlluminations(anyString()))
                .willThrow(exception);

        mockMvc.perform(get("/api/sensor/illumination/week")
                        .param("place", place))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", equalTo(message)));

    }

    @Test
    void getMonthlyIlluminations() throws Exception {
        // given
        Instant time = Instant.now().minus(30, ChronoUnit.DAYS);
        Integer maxIllumination = 100;
        Integer minIllumination = 50;
        String place = "test place";
        IlluminationMaxMin illuminationMaxMinDaily = new IlluminationMaxMin(time, maxIllumination, minIllumination);
        List<IlluminationMaxMin> illuminations = List.of(illuminationMaxMinDaily);

        // when
        given(illuminationService.getMonthlyIlluminations(anyString()))
                .willReturn(illuminations);

        //then
        mockMvc.perform(get("/api/sensor/illumination/month")
                        .param("place", place))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].time", equalTo(time.toString())))
                .andExpect(jsonPath("$[0].maxIllumination", equalTo(maxIllumination)))
                .andExpect(jsonPath("$[0].minIllumination", equalTo(minIllumination)));
    }

    @Test
    void getMonthlyIlluminationsException() throws Exception {
        String message = "조도를 찾을 수 없습니다.";
        String place = "test place";
        IlluminationNotFoundException exception = new IlluminationNotFoundException(message);
        given(illuminationService.getMonthlyIlluminations(anyString()))
                .willThrow(exception);

        mockMvc.perform(get("/api/sensor/illumination/month")
                        .param("place", place))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", equalTo(message)));
    }
}