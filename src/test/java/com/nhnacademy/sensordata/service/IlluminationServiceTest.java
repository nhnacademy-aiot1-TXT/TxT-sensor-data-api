package com.nhnacademy.sensordata.service;

import com.nhnacademy.sensordata.exception.IlluminationNotFoundException;
import com.nhnacademy.sensordata.measurement.illumination.Illumination;
import com.nhnacademy.sensordata.measurement.illumination.IlluminationMaxMinDaily;
import com.nhnacademy.sensordata.measurement.illumination.IlluminationMaxMinMonthly;
import com.nhnacademy.sensordata.measurement.illumination.IlluminationMaxMinWeekly;
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
class IlluminationServiceTest {
    @Autowired
    private IlluminationService illuminationService;
    @MockBean
    private InfluxDBUtil influxDBUtil;

    @Test
    void getIllumination() {
        // given
        Instant time = Instant.now();
        String device = "test device";
        String place = "test place";
        String topic = "test topic";
        Integer value = 65;

        Illumination illumination = new Illumination(time, device, place, topic, value);

        given(influxDBUtil.getSensorData(anyString(), eq(Illumination.class))).willReturn(Optional.of(illumination));

        // when
        Illumination resultIllumination = illuminationService.getIllumination();

        // then
        assertAll(
                () -> assertNotNull(resultIllumination),
                () -> assertEquals(time, resultIllumination.getTime()),
                () -> assertEquals(device, resultIllumination.getDevice()),
                () -> assertEquals(place, resultIllumination.getPlace()),
                () -> assertEquals(topic, resultIllumination.getTopic()),
                () -> assertEquals(value, resultIllumination.getValue())
        );
    }

    @Test
    void getIlluminationException() {
        given(influxDBUtil.getSensorData(anyString(), eq(Illumination.class))).willReturn(Optional.empty());

        assertThrows(IlluminationNotFoundException.class, () -> illuminationService.getIllumination());
    }

    @Test
    void getDailyIlluminations() {
        // given
        Instant time = Instant.now();
        Integer maxIllumination = 100;
        Integer minIllumination = 50;

        IlluminationMaxMinDaily illumination = new IlluminationMaxMinDaily(time, maxIllumination, minIllumination);

        given(influxDBUtil.getSensorDataList(any(), any(), anyString(), anyString(), eq(IlluminationMaxMinDaily.class))).willReturn(List.of(illumination));

        // when
        List<IlluminationMaxMinDaily> dailyIlluminations = illuminationService.getDailyIlluminations();

        // then
        assertAll(
                () -> assertNotNull(dailyIlluminations),
                () -> assertFalse(dailyIlluminations.isEmpty()),
                () -> assertEquals(time, dailyIlluminations.get(0).getTime()),
                () -> assertEquals(maxIllumination, dailyIlluminations.get(0).getMaxIllumination()),
                () -> assertEquals(minIllumination, dailyIlluminations.get(0).getMinIllumination())
        );
        ;
    }

    @Test
    void getWeeklyIlluminations() {
        // given
        Instant time = Instant.now();
        Integer dailyMaxIllumination = 100;
        Integer dailyMinIllumination = 50;
        Integer weeklyMaxIllumination = 90;
        Integer weeklyMinIllumination = 60;

        IlluminationMaxMinDaily illuminationMaxMinDaily = new IlluminationMaxMinDaily(time, dailyMaxIllumination, dailyMinIllumination);
        IlluminationMaxMinWeekly illuminationMaxMinWeekly = new IlluminationMaxMinWeekly(time, weeklyMaxIllumination, weeklyMinIllumination);

        // when
        given(influxDBUtil.getSensorDataList(any(), any(), anyString(), anyString(), eq(IlluminationMaxMinWeekly.class))).willReturn(new ArrayList<>(List.of(illuminationMaxMinWeekly)));
        given(influxDBUtil.getLastSensorData(anyString(), eq(IlluminationMaxMinDaily.class))).willReturn(Optional.of(illuminationMaxMinDaily));

        // then
        List<IlluminationMaxMinWeekly> weeklyIlluminations = illuminationService.getWeeklyIlluminations();

        assertAll(
                () -> assertNotNull(weeklyIlluminations),
                () -> assertFalse(weeklyIlluminations.isEmpty()),
                () -> assertTrue(weeklyIlluminations.size() >= 2),
                () -> assertEquals(time, weeklyIlluminations.get(0).getTime()),
                () -> assertEquals(time, weeklyIlluminations.get(1).getTime()),
                () -> assertEquals(weeklyMaxIllumination, weeklyIlluminations.get(0).getMaxIllumination()),
                () -> assertEquals(dailyMaxIllumination, weeklyIlluminations.get(1).getMaxIllumination()),
                () -> assertEquals(weeklyMinIllumination, weeklyIlluminations.get(0).getMinIllumination()),
                () -> assertEquals(dailyMinIllumination, weeklyIlluminations.get(1).getMinIllumination())
        );
    }

    @Test
    void getWeeklyIlluminationsException() {
        Instant time = Instant.now();
        Integer weeklyMaxIllumination = 100;
        Integer weeklyMinIllumination = 50;

        IlluminationMaxMinWeekly illuminationMaxMinWeekly = new IlluminationMaxMinWeekly(time, weeklyMaxIllumination, weeklyMinIllumination);

        given(influxDBUtil.getSensorDataList(any(), any(), anyString(), anyString(), eq(IlluminationMaxMinWeekly.class))).willReturn(new ArrayList<>(List.of(illuminationMaxMinWeekly)));
        given(influxDBUtil.getLastSensorData(anyString(), eq(IlluminationMaxMinDaily.class))).willReturn(Optional.empty());

        assertThrows(IlluminationNotFoundException.class, () -> illuminationService.getWeeklyIlluminations());
    }

    @Test
    void getMonthlyIlluminations() {
        // given
        Instant time = Instant.now();
        Integer dailyMaxIllumination = 100;
        Integer dailyMinIllumination = 50;
        Integer monthlyMaxIllumination = 90;
        Integer monthlyMinIllumination = 60;

        IlluminationMaxMinDaily illuminationMaxMinDaily = new IlluminationMaxMinDaily(time, dailyMaxIllumination, dailyMinIllumination);
        IlluminationMaxMinMonthly illuminationMaxMinMonthly = new IlluminationMaxMinMonthly(time, monthlyMaxIllumination, monthlyMinIllumination);

        // when
        given(influxDBUtil.getSensorDataList(any(), any(), anyString(), anyString(), eq(IlluminationMaxMinMonthly.class))).willReturn(new ArrayList<>(List.of(illuminationMaxMinMonthly)));
        given(influxDBUtil.getLastSensorData(anyString(), eq(IlluminationMaxMinDaily.class))).willReturn(Optional.of(illuminationMaxMinDaily));

        // then
        List<IlluminationMaxMinMonthly> monthlyIlluminations = illuminationService.getMonthlyIlluminations();

        assertAll(
                () -> assertNotNull(monthlyIlluminations),
                () -> assertFalse(monthlyIlluminations.isEmpty()),
                () -> assertTrue(monthlyIlluminations.size() >= 2),
                () -> assertEquals(time, monthlyIlluminations.get(0).getTime()),
                () -> assertEquals(time, monthlyIlluminations.get(1).getTime()),
                () -> assertEquals(monthlyMaxIllumination, monthlyIlluminations.get(0).getMaxIllumination()),
                () -> assertEquals(dailyMaxIllumination, monthlyIlluminations.get(1).getMaxIllumination()),
                () -> assertEquals(monthlyMinIllumination, monthlyIlluminations.get(0).getMinIllumination()),
                () -> assertEquals(dailyMinIllumination, monthlyIlluminations.get(1).getMinIllumination())
        );
    }

    @Test
    void getMonthlyIlluminationsException() {
        Instant time = Instant.now();
        Integer weeklyMaxIllumination = 100;
        Integer weeklyMinIllumination = 50;

        IlluminationMaxMinMonthly illuminationMaxMinMonthly = new IlluminationMaxMinMonthly(time, weeklyMaxIllumination, weeklyMinIllumination);

        given(influxDBUtil.getSensorDataList(any(), any(), anyString(), anyString(), eq(IlluminationMaxMinMonthly.class))).willReturn(new ArrayList<>(List.of(illuminationMaxMinMonthly)));
        given(influxDBUtil.getLastSensorData(anyString(), eq(IlluminationMaxMinDaily.class))).willReturn(Optional.empty());

        assertThrows(IlluminationNotFoundException.class, () -> illuminationService.getMonthlyIlluminations());
    }
}