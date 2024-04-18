package com.nhnacademy.sensordata.service;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.nhnacademy.sensordata.measurement.temperature.Temperature;
import com.nhnacademy.sensordata.measurement.temperature.TemperatureMaxMinDaily;
import com.nhnacademy.sensordata.measurement.temperature.TemperatureMaxMinMonthly;
import com.nhnacademy.sensordata.measurement.temperature.TemperatureMaxMinWeekly;
import com.nhnacademy.sensordata.exception.TemperatureNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class TemperatureServiceTest {
    @Autowired
    private TemperatureService temperatureService;
    @MockBean
    private InfluxDBClient influxDBClient;
    @Mock
    private QueryApi queryApi;

    @Test
    void getTemperature() {
        // given
        Instant time = Instant.now();
        String device = "test device";
        String place = "test place";
        String topic = "test topic";
        Float value = 20.0f;

        Temperature temperature = new Temperature(time, device, place, topic, value);

        given(influxDBClient.getQueryApi()).willReturn(queryApi);
        given(queryApi.query(anyString(), eq(Temperature.class))).willReturn(List.of(temperature));

        // when
        Temperature resultTemperature = temperatureService.getTemperature();

        // then
        assertAll(
                () -> assertNotNull(resultTemperature),
                () -> assertEquals(time, resultTemperature.getTime()),
                () -> assertEquals(device, resultTemperature.getDevice()),
                () -> assertEquals(place, resultTemperature.getPlace()),
                () -> assertEquals(topic, resultTemperature.getTopic()),
                () -> assertEquals(value, resultTemperature.getValue())
        );
    }

    @Test
    void getTemperatureException() {
        given(influxDBClient.getQueryApi()).willReturn(queryApi);
        given(queryApi.query(anyString(), eq(Temperature.class))).willReturn(Collections.emptyList());

        assertThrows(TemperatureNotFoundException.class, () -> temperatureService.getTemperature());
    }

    @Test
    void getDailyTemperatures() {
        // given
        Instant time = Instant.now();
        Float maxTemperature = 24.0f;
        Float minTemperature = 20.0f;

        TemperatureMaxMinDaily temperature = new TemperatureMaxMinDaily(time, maxTemperature, minTemperature);

        given(influxDBClient.getQueryApi()).willReturn(queryApi);
        given(queryApi.query(anyString(), eq(TemperatureMaxMinDaily.class))).willReturn(List.of(temperature));

        // when
        List<TemperatureMaxMinDaily> dailyTemperatures = temperatureService.getDailyTemperatures();

        // then
        assertAll(
                () -> assertNotNull(dailyTemperatures),
                () -> assertFalse(dailyTemperatures.isEmpty()),
                () -> assertEquals(time, dailyTemperatures.get(0).getTime()),
                () -> assertEquals(maxTemperature, dailyTemperatures.get(0).getMaxTemperature()),
                () -> assertEquals(minTemperature, dailyTemperatures.get(0).getMinTemperature())
        );
    }

    @Test
    void getWeeklyTemperatures() {
        // given
        Instant time = Instant.now();
        Float dailyMaxTemperature = 22.0f;
        Float dailyMinTemperature = 18.0f;
        Float weeklyMaxTemperature = 24.0f;
        Float weeklyMinTemperature = 20.0f;

        TemperatureMaxMinDaily temperatureMaxMinDaily = new TemperatureMaxMinDaily(time, dailyMaxTemperature, dailyMinTemperature);
        TemperatureMaxMinWeekly temperatureMaxMinWeekly = new TemperatureMaxMinWeekly(time, weeklyMaxTemperature, weeklyMinTemperature);

        // when
        given(influxDBClient.getQueryApi()).willReturn(queryApi);
        given(queryApi.query(anyString(), eq(TemperatureMaxMinWeekly.class))).willReturn(new ArrayList<>(List.of(temperatureMaxMinWeekly)));
        given(queryApi.query(anyString(), eq(TemperatureMaxMinDaily.class))).willReturn(List.of(temperatureMaxMinDaily));

        // then
        List<TemperatureMaxMinWeekly> weeklyTemperatures = temperatureService.getWeeklyTemperatures();

        assertAll(
                () -> assertNotNull(weeklyTemperatures),
                () -> assertFalse(weeklyTemperatures.isEmpty()),
                () -> assertTrue(weeklyTemperatures.size() >= 2),
                () -> assertEquals(time, weeklyTemperatures.get(0).getTime()),
                () -> assertEquals(time, weeklyTemperatures.get(1).getTime()),
                () -> assertEquals(weeklyMaxTemperature, weeklyTemperatures.get(0).getMaxTemperature()),
                () -> assertEquals(dailyMaxTemperature, weeklyTemperatures.get(1).getMaxTemperature()),
                () -> assertEquals(weeklyMinTemperature, weeklyTemperatures.get(0).getMinTemperature()),
                () -> assertEquals(dailyMinTemperature, weeklyTemperatures.get(1).getMinTemperature())
        );
    }

    @Test
    void getWeeklyTemperaturesException() {
        Instant time = Instant.now();
        Float weeklyMaxTemperature = 24.0f;
        Float weeklyMinTemperature = 20.0f;

        TemperatureMaxMinWeekly temperatureMaxMinWeekly = new TemperatureMaxMinWeekly(time, weeklyMaxTemperature, weeklyMinTemperature);

        given(influxDBClient.getQueryApi()).willReturn(queryApi);
        given(queryApi.query(anyString(), eq(TemperatureMaxMinWeekly.class))).willReturn(new ArrayList<>(List.of(temperatureMaxMinWeekly)));
        given(queryApi.query(anyString(), eq(TemperatureMaxMinDaily.class))).willReturn(Collections.emptyList());

        assertThrows(TemperatureNotFoundException.class, () -> temperatureService.getWeeklyTemperatures());
    }

    @Test
    void getMonthlyTemperatures() {
        // given
        Instant time = Instant.now();
        Float dailyMaxTemperature = 22.0f;
        Float dailyMinTemperature = 18.0f;
        Float monthlyMaxTemperature = 24.0f;
        Float monthlyMinTemperature = 20.0f;

        TemperatureMaxMinDaily temperatureMaxMinDaily = new TemperatureMaxMinDaily(time, dailyMaxTemperature, dailyMinTemperature);
        TemperatureMaxMinMonthly temperatureMaxMinMonthly = new TemperatureMaxMinMonthly(time, monthlyMaxTemperature, monthlyMinTemperature);

        // when
        given(influxDBClient.getQueryApi()).willReturn(queryApi);
        given(queryApi.query(anyString(), eq(TemperatureMaxMinMonthly.class))).willReturn(new ArrayList<>(List.of(temperatureMaxMinMonthly)));
        given(queryApi.query(anyString(), eq(TemperatureMaxMinDaily.class))).willReturn(List.of(temperatureMaxMinDaily));

        // then
        List<TemperatureMaxMinMonthly> monthlyTemperatures = temperatureService.getMonthlyTemperatures();

        assertAll(
                () -> assertNotNull(monthlyTemperatures),
                () -> assertFalse(monthlyTemperatures.isEmpty()),
                () -> assertTrue(monthlyTemperatures.size() >= 2),
                () -> assertEquals(time, monthlyTemperatures.get(0).getTime()),
                () -> assertEquals(time, monthlyTemperatures.get(1).getTime()),
                () -> assertEquals(monthlyMaxTemperature, monthlyTemperatures.get(0).getMaxTemperature()),
                () -> assertEquals(dailyMaxTemperature, monthlyTemperatures.get(1).getMaxTemperature()),
                () -> assertEquals(monthlyMinTemperature, monthlyTemperatures.get(0).getMinTemperature()),
                () -> assertEquals(dailyMinTemperature, monthlyTemperatures.get(1).getMinTemperature())
        );
    }

    @Test
    void getMonthlyTemperaturesException() {
        Instant time = Instant.now();
        Float weeklyMaxTemperature = 24.0f;
        Float weeklyMinTemperature = 20.0f;

        TemperatureMaxMinMonthly temperatureMaxMinMonthly = new TemperatureMaxMinMonthly(time, weeklyMaxTemperature, weeklyMinTemperature);

        given(influxDBClient.getQueryApi()).willReturn(queryApi);
        given(queryApi.query(anyString(), eq(TemperatureMaxMinMonthly.class))).willReturn(new ArrayList<>(List.of(temperatureMaxMinMonthly)));
        given(queryApi.query(anyString(), eq(TemperatureMaxMinDaily.class))).willReturn(Collections.emptyList());

        assertThrows(TemperatureNotFoundException.class, () -> temperatureService.getMonthlyTemperatures());
    }
}