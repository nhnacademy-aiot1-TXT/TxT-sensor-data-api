package com.nhnacademy.sensordata.service;

import com.nhnacademy.sensordata.exception.PeopleCountNotFoundException;
import com.nhnacademy.sensordata.measurement.people_count.PeopleCount;
import com.nhnacademy.sensordata.util.InfluxDBUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class PeopleCountServiceImplTest {
    @Autowired
    private PeopleCountService peopleCountService;
    @MockBean
    private InfluxDBUtil influxDBUtil;

    @Test
    void getPeopleCount() {
        Instant time = Instant.now();
        Integer count = 6;
        PeopleCount peopleCount = new PeopleCount(time, count);

        given(influxDBUtil.getSensorData(anyString(), anyString(), eq(PeopleCount.class))).willReturn(Optional.of(peopleCount));

        PeopleCount resultPeopleCount = peopleCountService.getPeopleCount();

        assertAll(
                () -> assertEquals(peopleCount.getTime(), resultPeopleCount.getTime()),
                () -> assertEquals(peopleCount.getCount(), resultPeopleCount.getCount())
        );
    }

    @Test
    void getPeopleCountException() {
        given(influxDBUtil.getSensorData(anyString(), anyString(), eq(PeopleCount.class))).willReturn(Optional.empty());

        assertThrows(PeopleCountNotFoundException.class, () -> peopleCountService.getPeopleCount());
    }
}