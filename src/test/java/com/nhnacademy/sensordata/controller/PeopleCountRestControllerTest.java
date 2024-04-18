package com.nhnacademy.sensordata.controller;

import com.nhnacademy.sensordata.measurement.people_count.PeopleCount;
import com.nhnacademy.sensordata.exception.PeopleCountNotFoundException;
import com.nhnacademy.sensordata.service.PeopleCountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class PeopleCountRestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PeopleCountService peopleCountService;

    @Test
    void getPeopleCount() throws Exception {
        Instant time = Instant.now();
        Integer inCount = 6;
        Integer outCount = 4;
        PeopleCount peopleCount = new PeopleCount(time, inCount, outCount);

        given(peopleCountService.getPeopleCount()).willReturn(peopleCount);

        mockMvc.perform(get("/api/people-count"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.time", equalTo(time.toString())))
                .andExpect(jsonPath("$.inCount", equalTo(inCount)))
                .andExpect(jsonPath("$.outCount", equalTo(outCount)))
                .andReturn();
    }

    @Test
    void getPeopleCountException() throws Exception {
        String message = "people-count를 찾을 수 없습니다.";
        given(peopleCountService.getPeopleCount())
                .willThrow(new PeopleCountNotFoundException(message));

        mockMvc.perform(get("/api/people-count"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", equalTo(message)));
    }
}