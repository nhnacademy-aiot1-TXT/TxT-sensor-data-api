package com.nhnacademy.sensordata.service;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.nhnacademy.sensordata.exception.PeopleCountNotFoundException;
import com.nhnacademy.sensordata.measurement.people_count.PeopleCount;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class PeopleCountServiceImplTest {
    @Autowired
    private PeopleCountService peopleCountService;
    @MockBean
    private InfluxDBClient influxDBClient;
    @Mock
    private QueryApi queryApi;

    @Test
    void getPeopleCount() {
        Instant time = Instant.now();
        Integer count = 6;
        PeopleCount peopleCount = new PeopleCount(time, count);

        given(influxDBClient.getQueryApi()).willReturn(queryApi);
        given(queryApi.query(anyString(), eq(PeopleCount.class))).willReturn(List.of(peopleCount));

        PeopleCount resultPeopleCount = peopleCountService.getPeopleCount();

        assertAll(
                () -> assertEquals(peopleCount.getTime(), resultPeopleCount.getTime()),
                () -> assertEquals(peopleCount.getCount(), resultPeopleCount.getCount())
        );
    }

    @Test
    void getPeopleCountException() {
        given(influxDBClient.getQueryApi()).willReturn(queryApi);
        given(queryApi.query(anyString(), eq(PeopleCount.class))).willReturn(Collections.emptyList());

        assertThrows(PeopleCountNotFoundException.class, () -> peopleCountService.getPeopleCount());
    }
}