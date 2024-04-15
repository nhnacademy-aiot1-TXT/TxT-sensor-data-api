package com.nhnacademy.sensordata.controller;

import com.nhnacademy.sensordata.entity.illumination.Illumination;
import com.nhnacademy.sensordata.entity.illumination.IlluminationMaxMinDaily;
import com.nhnacademy.sensordata.entity.illumination.IlluminationMaxMinMonthly;
import com.nhnacademy.sensordata.entity.illumination.IlluminationMaxMinWeekly;
import com.nhnacademy.sensordata.service.IlluminationService;
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
        given(illuminationService.getIllumination())
                .willReturn(illumination);

        //then
        mockMvc.perform(get("/api/illumination"))
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
    void getDailyIlluminations() throws Exception {
        // given
        Instant time = Instant.now();
        Integer maxIllumination = 100;
        Integer minIllumination = 50;
        IlluminationMaxMinDaily illuminationMaxMinDaily = new IlluminationMaxMinDaily(time, maxIllumination, minIllumination);
        List<IlluminationMaxMinDaily> illuminations = List.of(illuminationMaxMinDaily);

        // when
        given(illuminationService.getDailyIlluminations())
                .willReturn(illuminations);

        //then
        mockMvc.perform(get("/api/illumination/day"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].time", equalTo(time.toString())))
                .andExpect(jsonPath("$[0].maxIllumination", equalTo(maxIllumination)))
                .andExpect(jsonPath("$[0].minIllumination", equalTo(minIllumination)));
    }

    @Test
    void getWeeklyIlluminations() throws Exception {
        // given
        Instant time = Instant.now().minus(7, ChronoUnit.DAYS);
        Integer maxIllumination = 100;
        Integer minIllumination = 50;
        IlluminationMaxMinWeekly illuminationMaxMinDaily = new IlluminationMaxMinWeekly(time, maxIllumination, minIllumination);
        List<IlluminationMaxMinWeekly> illuminations = List.of(illuminationMaxMinDaily);

        // when
        given(illuminationService.getWeeklyIlluminations())
                .willReturn(illuminations);

        //then
        mockMvc.perform(get("/api/illumination/week"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].time", equalTo(time.toString())))
                .andExpect(jsonPath("$[0].maxIllumination", equalTo(maxIllumination)))
                .andExpect(jsonPath("$[0].minIllumination", equalTo(minIllumination)));
    }

    @Test
    void getMonthlyIlluminations() throws Exception {
        // given
        Instant time = Instant.now().minus(30, ChronoUnit.DAYS);
        Integer maxIllumination = 100;
        Integer minIllumination = 50;
        IlluminationMaxMinMonthly illuminationMaxMinDaily = new IlluminationMaxMinMonthly(time, maxIllumination, minIllumination);
        List<IlluminationMaxMinMonthly> illuminations = List.of(illuminationMaxMinDaily);

        // when
        given(illuminationService.getMonthlyIlluminations())
                .willReturn(illuminations);

        //then
        mockMvc.perform(get("/api/illumination/month"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].time", equalTo(time.toString())))
                .andExpect(jsonPath("$[0].maxIllumination", equalTo(maxIllumination)))
                .andExpect(jsonPath("$[0].minIllumination", equalTo(minIllumination)));
    }
}