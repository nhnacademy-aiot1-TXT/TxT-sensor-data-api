package com.nhnacademy.sensordata.service;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.nhnacademy.sensordata.entity.illumination.Illumination;
import com.nhnacademy.sensordata.entity.illumination.IlluminationMaxMinDaily;
import com.nhnacademy.sensordata.entity.illumination.IlluminationMaxMinMonthly;
import com.nhnacademy.sensordata.entity.illumination.IlluminationMaxMinWeekly;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@Disabled
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class IlluminationServiceTest {
    @Autowired
    private IlluminationService illuminationService;
    @MockBean
    private InfluxDBClient influxDBClient;
    @Mock
    private QueryApi queryApi;

    @Test
    void getIllumination() {
        // given
        Instant time = Instant.now();
        String device = "test device";
        String place = "test place";
        String topic = "test topic";
        Integer value = 65;

        Illumination illumination = new Illumination(time, device, place, topic, value);

        given(influxDBClient.getQueryApi()).willReturn(queryApi);
        given(queryApi.query(anyString(), eq(Illumination.class))).willReturn(List.of(illumination));

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
    void getDailyIlluminations() {
        // given
        Instant time = Instant.now();
        Integer maxIllumination = 100;
        Integer minIllumination = 50;

        IlluminationMaxMinDaily illumination = new IlluminationMaxMinDaily(time, maxIllumination, minIllumination);

        given(influxDBClient.getQueryApi()).willReturn(queryApi);
        given(queryApi.query(anyString(), eq(IlluminationMaxMinDaily.class))).willReturn(List.of(illumination));

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
        given(influxDBClient.getQueryApi()).willReturn(queryApi);
        given(queryApi.query(anyString(), eq(IlluminationMaxMinWeekly.class))).willReturn(new ArrayList<>(List.of(illuminationMaxMinWeekly)));
        given(queryApi.query(anyString(), eq(IlluminationMaxMinDaily.class))).willReturn(List.of(illuminationMaxMinDaily));

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
        ;
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
        given(influxDBClient.getQueryApi()).willReturn(queryApi);
        given(queryApi.query(anyString(), eq(IlluminationMaxMinMonthly.class))).willReturn(new ArrayList<>(List.of(illuminationMaxMinMonthly)));
        given(queryApi.query(anyString(), eq(IlluminationMaxMinDaily.class))).willReturn(List.of(illuminationMaxMinDaily));

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
}