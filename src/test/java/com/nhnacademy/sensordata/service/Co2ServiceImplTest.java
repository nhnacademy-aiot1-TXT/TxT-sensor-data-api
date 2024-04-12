package com.nhnacademy.sensordata.service;

import com.nhnacademy.sensordata.entity.co2.Co2;
import com.nhnacademy.sensordata.entity.co2.Co2MaxMinDaily;
import com.nhnacademy.sensordata.entity.co2.Co2MaxMinMonthly;
import com.nhnacademy.sensordata.entity.co2.Co2MaxMinWeekly;
import com.nhnacademy.sensordata.utils.InfluxDBUtil;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class Co2ServiceImplTest {
    @Autowired
    private Co2Service co2Service;
    @MockBean
    private InfluxDBResultMapper resultMapper;
    @MockBean
    private InfluxDBUtil influxDBUtil;

    @Test
    void getCo2() {
        Instant time = Instant.now();
        String device = "test device";
        String place = "test place";
        String topic = "test topic";
        Integer value = 20;
        Co2 co2 = new Co2(time, device, place, topic, value);

        given(influxDBUtil.processingQuery(any())).willReturn(new QueryResult());
        given(resultMapper.toPOJO(any(), any())).willReturn(List.of(co2));

        Co2 resultCo2 = co2Service.getCo2();

        assertAll(
                () -> assertEquals(co2.getTime(), resultCo2.getTime()),
                () -> assertEquals(co2.getDevice(), resultCo2.getDevice()),
                () -> assertEquals(co2.getPlace(), resultCo2.getPlace()),
                () -> assertEquals(co2.getTopic(), resultCo2.getTopic()),
                () -> assertEquals(co2.getValue(), resultCo2.getValue())
        );
    }

    @Test
    void getDailyCo2() {
        Instant time = Instant.now();
        Integer maxCo2 = 80;
        Integer minCo2 = 60;
        Co2MaxMinDaily co2Daily = new Co2MaxMinDaily(time, maxCo2, minCo2);

        given(influxDBUtil.processingQuery(any())).willReturn(new QueryResult());
        given(resultMapper.toPOJO(any(), any())).willReturn(List.of(co2Daily));

        Co2MaxMinDaily resultCo2 = co2Service.getDailyCo2().get(0);

        assertAll(
                () -> assertEquals(co2Daily.getTime().plus(9, ChronoUnit.HOURS), resultCo2.getTime()),
                () -> assertEquals(co2Daily.getMaxCo2(), resultCo2.getMaxCo2()),
                () -> assertEquals(co2Daily.getMinCo2(), resultCo2.getMinCo2())
        );
    }

    @Test
    void getWeeklyCo2() {
        Instant time = Instant.now();
        Integer weeklyMaxCo2 = 80;
        Integer weeklyMinCo2 = 60;
        Integer dailyMaxCo2 = 80;
        Integer dailyMinCo2 = 60;
        Co2MaxMinWeekly co2Weekly = new Co2MaxMinWeekly(time, weeklyMaxCo2, weeklyMinCo2);
        Co2MaxMinDaily co2Daily = new Co2MaxMinDaily(time, dailyMaxCo2, dailyMinCo2);

        given(influxDBUtil.processingQuery(any())).willReturn(new QueryResult());
        given(resultMapper.toPOJO(any(), eq(Co2MaxMinWeekly.class))).willReturn(List.of(co2Weekly));
        given(resultMapper.toPOJO(any(), eq(Co2MaxMinDaily.class))).willReturn(List.of(co2Daily));

        List<Co2MaxMinWeekly> resultCo2 = co2Service.getWeeklyCo2();

        assertAll(
                () -> assertEquals(co2Weekly.getTime().plus(9, ChronoUnit.HOURS), resultCo2.get(0).getTime()),
                () -> assertEquals(co2Daily.getTime().plus(9, ChronoUnit.HOURS), resultCo2.get(1).getTime()),
                () -> assertEquals(co2Weekly.getMaxCo2(), resultCo2.get(0).getMaxCo2()),
                () -> assertEquals(co2Weekly.getMinCo2(), resultCo2.get(0).getMinCo2()),
                () -> assertEquals(co2Daily.getMaxCo2(), resultCo2.get(1).getMaxCo2()),
                () -> assertEquals(co2Daily.getMinCo2(), resultCo2.get(1).getMinCo2())
        );
    }

    @Test
    void getMonthlyCo2() {
        Instant time = Instant.now();
        Integer monthlyMaxCo2 = 80;
        Integer monthlyMinCo2 = 60;
        Integer dailyMaxCo2 = 80;
        Integer dailyMinCo2 = 60;
        Co2MaxMinMonthly co2Weekly = new Co2MaxMinMonthly(time, monthlyMaxCo2, monthlyMinCo2);
        Co2MaxMinDaily co2Daily = new Co2MaxMinDaily(time, dailyMaxCo2, dailyMinCo2);

        given(influxDBUtil.processingQuery(any())).willReturn(new QueryResult());
        given(resultMapper.toPOJO(any(), eq(Co2MaxMinMonthly.class))).willReturn(List.of(co2Weekly));
        given(resultMapper.toPOJO(any(), eq(Co2MaxMinDaily.class))).willReturn(List.of(co2Daily));

        List<Co2MaxMinMonthly> resultCo2 = co2Service.getMonthlyCo2();

        assertAll(
                () -> assertEquals(co2Weekly.getTime().plus(9, ChronoUnit.HOURS), resultCo2.get(0).getTime()),
                () -> assertEquals(co2Daily.getTime().plus(9, ChronoUnit.HOURS), resultCo2.get(1).getTime()),
                () -> assertEquals(co2Weekly.getMaxCo2(), resultCo2.get(0).getMaxCo2()),
                () -> assertEquals(co2Weekly.getMinCo2(), resultCo2.get(0).getMinCo2()),
                () -> assertEquals(co2Daily.getMaxCo2(), resultCo2.get(1).getMaxCo2()),
                () -> assertEquals(co2Daily.getMinCo2(), resultCo2.get(1).getMinCo2())
        );
    }


}