package com.nhnacademy.sensordata.service;

import com.nhnacademy.sensordata.exception.Co2NotFoundException;
import com.nhnacademy.sensordata.measurement.co2.Co2;
import com.nhnacademy.sensordata.measurement.co2.Co2MaxMin;
import com.nhnacademy.sensordata.util.InfluxDBUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class Co2ServiceImplTest {
    @Autowired
    private Co2Service co2Service;
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

        given(influxDBUtil.getSensorData(anyString(), anyString(), eq(Co2.class))).willReturn(Optional.of(co2));

        Co2 resultCo2 = co2Service.getCo2(place);

        assertAll(
                () -> assertEquals(co2.getTime(), resultCo2.getTime()),
                () -> assertEquals(co2.getDevice(), resultCo2.getDevice()),
                () -> assertEquals(co2.getPlace(), resultCo2.getPlace()),
                () -> assertEquals(co2.getTopic(), resultCo2.getTopic()),
                () -> assertEquals(co2.getValue(), resultCo2.getValue())
        );
    }

    @Test
    void getCo2Exception() {
        String place = "test place";

        given(influxDBUtil.getSensorData(anyString(), anyString(), eq(Co2.class))).willReturn(Optional.empty());

        assertThrows(Co2NotFoundException.class, () -> co2Service.getCo2(place));
    }

    @Test
    void getDailyCo2() {
        Instant time = Instant.now();
        Integer maxCo2 = 80;
        Integer minCo2 = 60;
        String place = "test place";
        Co2MaxMin co2Daily = new Co2MaxMin(time, maxCo2, minCo2);

        given(influxDBUtil.getSensorDataList(any(), any(), anyString(), anyString(), eq(Co2MaxMin.class))).willReturn(List.of(co2Daily));

        Co2MaxMin resultCo2 = co2Service.getDailyCo2(place).get(0);

        assertAll(
                () -> assertEquals(co2Daily.getTime(), resultCo2.getTime()),
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
        Co2MaxMin co2Weekly = new Co2MaxMin(time, weeklyMaxCo2, weeklyMinCo2);
        Co2MaxMin co2Daily = new Co2MaxMin(time, dailyMaxCo2, dailyMinCo2);

        given(influxDBUtil.getSensorDataList(any(), any(), anyString(), anyString(), eq(Co2MaxMin.class))).willReturn(new ArrayList<>(List.of(co2Weekly)));
        given(influxDBUtil.getLastSensorData(any(), anyString(), eq(Co2MaxMin.class))).willReturn(Optional.of(co2Daily));

        List<Co2MaxMin> resultCo2 = co2Service.getWeeklyCo2();

        assertAll(
                () -> assertEquals(co2Weekly.getTime(), resultCo2.get(0).getTime()),
                () -> assertEquals(co2Daily.getTime(), resultCo2.get(1).getTime()),
                () -> assertEquals(co2Weekly.getMaxCo2(), resultCo2.get(0).getMaxCo2()),
                () -> assertEquals(co2Weekly.getMinCo2(), resultCo2.get(0).getMinCo2()),
                () -> assertEquals(co2Daily.getMaxCo2(), resultCo2.get(1).getMaxCo2()),
                () -> assertEquals(co2Daily.getMinCo2(), resultCo2.get(1).getMinCo2())
        );
    }

    @Test
    void getWeeklyCo2Exception() {
        given(influxDBUtil.getLastSensorData(any(), anyString(), eq(Co2MaxMin.class))).willReturn(Optional.empty());

        assertThrows(Co2NotFoundException.class, () -> co2Service.getWeeklyCo2());
    }

    @Test
    void getMonthlyCo2() {
        Instant time = Instant.now();
        Integer monthlyMaxCo2 = 80;
        Integer monthlyMinCo2 = 60;
        Integer dailyMaxCo2 = 80;
        Integer dailyMinCo2 = 60;
        Co2MaxMin co2Monthly = new Co2MaxMin(time, monthlyMaxCo2, monthlyMinCo2);
        Co2MaxMin co2Daily = new Co2MaxMin(time, dailyMaxCo2, dailyMinCo2);

        given(influxDBUtil.getSensorDataList(any(), any(), anyString(), anyString(), eq(Co2MaxMin.class))).willReturn(new ArrayList<>(List.of(co2Monthly)));
        given(influxDBUtil.getLastSensorData(any(), anyString(), eq(Co2MaxMin.class))).willReturn(Optional.of(co2Daily));

        List<Co2MaxMin> resultCo2 = co2Service.getMonthlyCo2();

        assertAll(
                () -> assertEquals(co2Monthly.getTime(), resultCo2.get(0).getTime()),
                () -> assertEquals(co2Daily.getTime(), resultCo2.get(1).getTime()),
                () -> assertEquals(co2Monthly.getMaxCo2(), resultCo2.get(0).getMaxCo2()),
                () -> assertEquals(co2Monthly.getMinCo2(), resultCo2.get(0).getMinCo2()),
                () -> assertEquals(co2Daily.getMaxCo2(), resultCo2.get(1).getMaxCo2()),
                () -> assertEquals(co2Daily.getMinCo2(), resultCo2.get(1).getMinCo2())
        );
    }

    @Test
    void getMonthlyCo2Exception() {
        given(influxDBUtil.getLastSensorData(any(), anyString(), eq(Co2MaxMin.class))).willReturn(Optional.empty());

        assertThrows(Co2NotFoundException.class, () -> co2Service.getMonthlyCo2());
    }
}