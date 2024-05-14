package com.nhnacademy.sensordata.service;

import com.nhnacademy.sensordata.exception.TemperatureNotFoundException;
import com.nhnacademy.sensordata.measurement.temperature.Temperature;
import com.nhnacademy.sensordata.measurement.temperature.TemperatureMaxMin;
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
class TemperatureServiceTest {
    @Autowired
    private TemperatureService temperatureService;
    @MockBean
    private InfluxDBUtil influxDBUtil;

    @Test
    void getTemperature() {
        // given
        Instant time = Instant.now();
        String device = "test device";
        String place = "test place";
        String topic = "test topic";
        Float value = 20.0f;

        Temperature temperature = new Temperature(time, device, place, topic, value);

        given(influxDBUtil.getSensorData(anyString(), eq(Temperature.class))).willReturn(Optional.of(temperature));

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
        given(influxDBUtil.getSensorData(anyString(), eq(Temperature.class))).willReturn(Optional.empty());

        assertThrows(TemperatureNotFoundException.class, () -> temperatureService.getTemperature());
    }

    @Test
    void getDailyTemperatures() {
        // given
        Instant time = Instant.now();
        Float maxTemperature = 24.0f;
        Float minTemperature = 20.0f;
        String place = "test place";

        TemperatureMaxMin temperature = new TemperatureMaxMin(time, maxTemperature, minTemperature);

        given(influxDBUtil.getSensorDataList(any(), any(), anyString(), anyString(), eq(TemperatureMaxMin.class))).willReturn(List.of(temperature));

        // when
        List<TemperatureMaxMin> dailyTemperatures = temperatureService.getDailyTemperatures(place);

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

        TemperatureMaxMin temperatureMaxMinDaily = new TemperatureMaxMin(time, dailyMaxTemperature, dailyMinTemperature);
        TemperatureMaxMin temperatureMaxMinWeekly = new TemperatureMaxMin(time, weeklyMaxTemperature, weeklyMinTemperature);

        // when
        given(influxDBUtil.getSensorDataList(any(), any(), anyString(), anyString(), eq(TemperatureMaxMin.class))).willReturn(new ArrayList<>(List.of(temperatureMaxMinWeekly)));
        given(influxDBUtil.getLastSensorData(any(), anyString(), eq(TemperatureMaxMin.class))).willReturn(Optional.of(temperatureMaxMinDaily));

        // then
        List<TemperatureMaxMin> weeklyTemperatures = temperatureService.getWeeklyTemperatures();

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

        TemperatureMaxMin temperatureMaxMinWeekly = new TemperatureMaxMin(time, weeklyMaxTemperature, weeklyMinTemperature);

        given(influxDBUtil.getSensorDataList(any(), any(), anyString(), anyString(), eq(TemperatureMaxMin.class))).willReturn(new ArrayList<>(List.of(temperatureMaxMinWeekly)));
        given(influxDBUtil.getLastSensorData(any(), anyString(), eq(TemperatureMaxMin.class))).willReturn(Optional.empty());

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

        TemperatureMaxMin temperatureMaxMinDaily = new TemperatureMaxMin(time, dailyMaxTemperature, dailyMinTemperature);
        TemperatureMaxMin temperatureMaxMinMonthly = new TemperatureMaxMin(time, monthlyMaxTemperature, monthlyMinTemperature);

        // when
        given(influxDBUtil.getSensorDataList(any(), any(), anyString(), anyString(), eq(TemperatureMaxMin.class))).willReturn(new ArrayList<>(List.of(temperatureMaxMinMonthly)));
        given(influxDBUtil.getLastSensorData(any(), anyString(), eq(TemperatureMaxMin.class))).willReturn(Optional.of(temperatureMaxMinDaily));

        // then
        List<TemperatureMaxMin> monthlyTemperatures = temperatureService.getMonthlyTemperatures();

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

        TemperatureMaxMin temperatureMaxMinMonthly = new TemperatureMaxMin(time, weeklyMaxTemperature, weeklyMinTemperature);

        given(influxDBUtil.getSensorDataList(any(), any(), anyString(), anyString(), eq(TemperatureMaxMin.class))).willReturn(new ArrayList<>(List.of(temperatureMaxMinMonthly)));
        given(influxDBUtil.getLastSensorData(any(), anyString(), eq(TemperatureMaxMin.class))).willReturn(Optional.empty());

        assertThrows(TemperatureNotFoundException.class, () -> temperatureService.getMonthlyTemperatures());
    }
}