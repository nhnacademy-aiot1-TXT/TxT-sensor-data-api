package com.nhnacademy.sensordata.controller;

import com.nhnacademy.sensordata.exception.VocNotFoundException;
import com.nhnacademy.sensordata.measurement.voc.Voc;
import com.nhnacademy.sensordata.service.VocService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class VocRestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private VocService vocService;

    @Test
    void getVoc() throws Exception {
        // given
        Instant time = Instant.now();
        String device = "test device";
        String place = "test place";
        String topic = "test topic";
        Integer value = 20;
        Voc voc = new Voc(time, device, place, topic, value);

        given(vocService.getVoc(anyString())).willReturn(voc);

        // when
        // then
        mockMvc.perform(get("/api/sensor/voc")
                        .param("place", place))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.time", equalTo(time.toString())))
                .andExpect(jsonPath("$.device", equalTo(device)))
                .andExpect(jsonPath("$.place", equalTo(place)))
                .andExpect(jsonPath("$.topic", equalTo(topic)))
                .andExpect(jsonPath("$.value", equalTo(value)));
    }

    @Test
    void getVocException() throws Exception {
        String place = "test place";
        String message = "voc를 찾을 수 없습니다.";
        VocNotFoundException exception = new VocNotFoundException(message);
        given(vocService.getVoc(anyString()))
                .willThrow(exception);

        mockMvc.perform(get("/api/sensor/voc")
                        .param("place", place))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", equalTo(message)));
    }
}