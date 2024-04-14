package com.nhnacademy.sensordata.service;

import com.nhnacademy.sensordata.entity.people_count.PeopleCount;
import com.nhnacademy.sensordata.utils.InfluxDBUtil;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class PeopleCountServiceImplTest {
    @Autowired
    private PeopleCountService peopleCountService;
    @MockBean
    private InfluxDBUtil influxDBUtil;
    @MockBean
    private InfluxDBResultMapper resultMapper;

    @Test
    void getPeopleCount() {
        Instant time = Instant.now();
        Integer inCount = 6;
        Integer outCount = 4;
        PeopleCount peopleCount = new PeopleCount(time, inCount, outCount);

        given(influxDBUtil.processingQuery(any())).willReturn(new QueryResult());
        given(resultMapper.toPOJO(any(), any())).willReturn(List.of(peopleCount));

        PeopleCount resultPeopleCount = peopleCountService.getPeopleCount();

        assertAll(
                () -> assertEquals(peopleCount.getTime(), resultPeopleCount.getTime()),
                () -> assertEquals(peopleCount.getInCount(), resultPeopleCount.getInCount()),
                () -> assertEquals(peopleCount.getOutCount(), resultPeopleCount.getOutCount())
        );
    }
}