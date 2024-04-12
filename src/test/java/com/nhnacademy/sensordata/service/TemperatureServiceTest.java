package com.nhnacademy.sensordata.service;

import com.nhnacademy.sensordata.entity.Temperature;
import com.nhnacademy.sensordata.entity.TemperatureMaxMinDaily;
import com.nhnacademy.sensordata.entity.TemperatureMaxMinMonthly;
import com.nhnacademy.sensordata.entity.TemperatureMaxMinWeekly;
import org.influxdb.dto.Point;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.influxdb.InfluxDBTemplate;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class TemperatureServiceTest {
    @Autowired
    private TemperatureService temperatureService;
    @MockBean(name = "influxDBTemplate")
    private InfluxDBTemplate<Point> influxDBTemplate;
    @MockBean
    private InfluxDBResultMapper resultMapper;

    @Test
    void getTemperature() {
        // given
        Instant time = Instant.now();
        String device = "test device";
        String place = "test place";
        String topic = "test topic";
        Double value = 20.0;

        Temperature temperature = new Temperature(time, device, place, topic, value);

        Mockito.when(influxDBTemplate.query(any())).thenReturn(new QueryResult());
        Mockito.when(resultMapper.toPOJO(any(), any())).thenReturn(List.of(temperature));

        // when
        Temperature resultTemperature = temperatureService.getTemperature();

        // then
        assertAll(
                () -> assertNotNull(resultTemperature),
                () -> assertEquals(time.plus(9, ChronoUnit.HOURS), resultTemperature.getTime()),
                () -> assertEquals(device, resultTemperature.getDevice()),
                () -> assertEquals(place, resultTemperature.getPlace()),
                () -> assertEquals(topic, resultTemperature.getTopic()),
                () -> assertEquals(value, resultTemperature.getValue())
        );
    }

    @Test
    void getDailyTemperatures() {
        // given
        Instant time = Instant.now();
        Double maxTemperature = 24.0;
        Double minTemperature = 20.0;

        TemperatureMaxMinDaily temperature = new TemperatureMaxMinDaily(time, maxTemperature, minTemperature);

        Mockito.when(influxDBTemplate.query(any())).thenReturn(new QueryResult());
        Mockito.when(resultMapper.toPOJO(any(), any())).thenReturn(List.of(temperature));

        // when
        List<TemperatureMaxMinDaily> dailyTemperatures = temperatureService.getDailyTemperatures();

        // then
        assertAll(
                () -> assertNotNull(dailyTemperatures),
                () -> assertFalse(dailyTemperatures.isEmpty()),
                () -> assertEquals(time.plus(9, ChronoUnit.HOURS), dailyTemperatures.get(0).getTime()),
                () -> assertEquals(maxTemperature, dailyTemperatures.get(0).getMaxTemperature()),
                () -> assertEquals(minTemperature, dailyTemperatures.get(0).getMinTemperature())
        );
    }

    @Test
    void getWeeklyTemperatures() {
        // given
        Instant time = Instant.now();
        Double dailyMaxTemperature = 22.0;
        Double dailyMinTemperature = 18.0;
        Double weeklyMaxTemperature = 24.0;
        Double weeklyMinTemperature = 20.0;

        TemperatureMaxMinDaily temperatureMaxMinDaily = new TemperatureMaxMinDaily(time, dailyMaxTemperature, dailyMinTemperature);
        TemperatureMaxMinWeekly temperatureMaxMinWeekly = new TemperatureMaxMinWeekly(time, weeklyMaxTemperature, weeklyMinTemperature);

        // when
        Mockito.when(influxDBTemplate.query(any())).thenReturn(new QueryResult());
        Mockito.when(resultMapper.toPOJO(any(), eq(TemperatureMaxMinWeekly.class))).thenReturn(List.of(temperatureMaxMinWeekly));
        Mockito.when(resultMapper.toPOJO(any(), eq(TemperatureMaxMinDaily.class))).thenReturn(List.of(temperatureMaxMinDaily));

        // then
        List<TemperatureMaxMinWeekly> weeklyTemperatures = temperatureService.getWeeklyTemperatures();

        assertAll(
                () -> assertNotNull(weeklyTemperatures),
                () -> assertFalse(weeklyTemperatures.isEmpty()),
                () -> assertTrue(weeklyTemperatures.size() >= 2),
                () -> assertEquals(time.plus(9, ChronoUnit.HOURS), weeklyTemperatures.get(0).getTime()),
                () -> assertEquals(time.plus(9, ChronoUnit.HOURS), weeklyTemperatures.get(1).getTime()),
                () -> assertEquals(weeklyMaxTemperature, weeklyTemperatures.get(0).getMaxTemperature()),
                () -> assertEquals(dailyMaxTemperature, weeklyTemperatures.get(1).getMaxTemperature()),
                () -> assertEquals(weeklyMinTemperature, weeklyTemperatures.get(0).getMinTemperature()),
                () -> assertEquals(dailyMinTemperature, weeklyTemperatures.get(1).getMinTemperature())
        );
    }

    @Test
    void getMonthlyTemperatures() {
        // given
        Instant time = Instant.now();
        Double dailyMaxTemperature = 22.0;
        Double dailyMinTemperature = 18.0;
        Double monthlyMaxTemperature = 24.0;
        Double monthlyMinTemperature = 20.0;

        TemperatureMaxMinDaily temperatureMaxMinDaily = new TemperatureMaxMinDaily(time, dailyMaxTemperature, dailyMinTemperature);
        TemperatureMaxMinMonthly temperatureMaxMinMonthly = new TemperatureMaxMinMonthly(time, monthlyMaxTemperature, monthlyMinTemperature);

        // when
        Mockito.when(influxDBTemplate.query(any())).thenReturn(new QueryResult());
        Mockito.when(resultMapper.toPOJO(any(), eq(TemperatureMaxMinMonthly.class))).thenReturn(List.of(temperatureMaxMinMonthly));
        Mockito.when(resultMapper.toPOJO(any(), eq(TemperatureMaxMinDaily.class))).thenReturn(List.of(temperatureMaxMinDaily));

        // then
        List<TemperatureMaxMinMonthly> monthlyTemperatures = temperatureService.getMonthlyTemperatures();

        assertAll(
                () -> assertNotNull(monthlyTemperatures),
                () -> assertFalse(monthlyTemperatures.isEmpty()),
                () -> assertTrue(monthlyTemperatures.size() >= 2),
                () -> assertEquals(time.plus(9, ChronoUnit.HOURS), monthlyTemperatures.get(0).getTime()),
                () -> assertEquals(time.plus(9, ChronoUnit.HOURS), monthlyTemperatures.get(1).getTime()),
                () -> assertEquals(monthlyMaxTemperature, monthlyTemperatures.get(0).getMaxTemperature()),
                () -> assertEquals(dailyMaxTemperature, monthlyTemperatures.get(1).getMaxTemperature()),
                () -> assertEquals(monthlyMinTemperature, monthlyTemperatures.get(0).getMinTemperature()),
                () -> assertEquals(dailyMinTemperature, monthlyTemperatures.get(1).getMinTemperature())
        );
    }
}