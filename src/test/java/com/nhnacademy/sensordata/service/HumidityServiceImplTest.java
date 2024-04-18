package com.nhnacademy.sensordata.service;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.nhnacademy.sensordata.measurement.humidity.Humidity;
import com.nhnacademy.sensordata.measurement.humidity.HumidityMaxMinDaily;
import com.nhnacademy.sensordata.measurement.humidity.HumidityMaxMinMonthly;
import com.nhnacademy.sensordata.measurement.humidity.HumidityMaxMinWeekly;
import com.nhnacademy.sensordata.exception.HumidityNotFoundException;
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
class HumidityServiceImplTest {
    @Autowired
    private HumidityService humidityService;
    @MockBean
    private InfluxDBClient influxDBClient;
    @Mock
    private QueryApi queryApi;

    @Test
    void getHumidity() {
        Instant time = Instant.now();
        String device = "test device";
        String place = "test place";
        String topic = "test topic";
        float value = 20.0f;
        Humidity humidity = new Humidity(time, device, place, topic, value);

        given(influxDBClient.getQueryApi()).willReturn(queryApi);
        given(queryApi.query(anyString(), eq(Humidity.class))).willReturn(List.of(humidity));

        Humidity resultHumidity = humidityService.getHumidity();

        assertAll(
                () -> assertEquals(humidity.getTime(), resultHumidity.getTime()),
                () -> assertEquals(humidity.getDevice(), resultHumidity.getDevice()),
                () -> assertEquals(humidity.getPlace(), resultHumidity.getPlace()),
                () -> assertEquals(humidity.getTopic(), resultHumidity.getTopic()),
                () -> assertEquals(humidity.getValue(), resultHumidity.getValue())
        );
    }

    @Test
    void getHumidityException() {
        given(influxDBClient.getQueryApi()).willReturn(queryApi);
        given(queryApi.query(anyString(), eq(Humidity.class))).willReturn(Collections.emptyList());

        assertThrows(HumidityNotFoundException.class, () -> humidityService.getHumidity());
    }

    @Test
    void getDailyHumidity() {
        Instant time = Instant.now();
        float maxHumidity = 80.0f;
        float minHumidity = 60.0f;
        HumidityMaxMinDaily humidityDaily = new HumidityMaxMinDaily(time, maxHumidity, minHumidity);

        given(influxDBClient.getQueryApi()).willReturn(queryApi);
        given(queryApi.query(anyString(), eq(HumidityMaxMinDaily.class))).willReturn(List.of(humidityDaily));

        HumidityMaxMinDaily resultHumidity = humidityService.getDailyHumidity().get(0);

        assertAll(
                () -> assertEquals(humidityDaily.getTime(), resultHumidity.getTime()),
                () -> assertEquals(humidityDaily.getMaxHumidity(), resultHumidity.getMaxHumidity()),
                () -> assertEquals(humidityDaily.getMinHumidity(), resultHumidity.getMinHumidity())
        );
    }

    @Test
    void getWeeklyHumidity() {
        Instant time = Instant.now();
        float weeklyMaxHumidity = 80.0f;
        float weeklyMinHumidity = 60.0f;
        float dailyMaxHumidity = 80.0f;
        float dailyMinHumidity = 60.0f;
        HumidityMaxMinWeekly humidityWeekly = new HumidityMaxMinWeekly(time, weeklyMaxHumidity, weeklyMinHumidity);
        HumidityMaxMinDaily humidityDaily = new HumidityMaxMinDaily(time, dailyMaxHumidity, dailyMinHumidity);

        given(influxDBClient.getQueryApi()).willReturn(queryApi);
        given(queryApi.query(anyString(), eq(HumidityMaxMinWeekly.class))).willReturn(new ArrayList<>(List.of(humidityWeekly)));
        given(queryApi.query(anyString(), eq(HumidityMaxMinDaily.class))).willReturn(new ArrayList<>(List.of(humidityDaily)));

        List<HumidityMaxMinWeekly> resultHumidity = humidityService.getWeeklyHumidity();

        assertAll(
                () -> assertEquals(humidityWeekly.getTime(), resultHumidity.get(0).getTime()),
                () -> assertEquals(humidityDaily.getTime(), resultHumidity.get(1).getTime()),
                () -> assertEquals(humidityWeekly.getMaxHumidity(), resultHumidity.get(0).getMaxHumidity()),
                () -> assertEquals(humidityWeekly.getMinHumidity(), resultHumidity.get(0).getMinHumidity()),
                () -> assertEquals(humidityDaily.getMaxHumidity(), resultHumidity.get(1).getMaxHumidity()),
                () -> assertEquals(humidityDaily.getMinHumidity(), resultHumidity.get(1).getMinHumidity())
        );
    }

    @Test
    void getWeeklyHumidityException() {
        given(influxDBClient.getQueryApi()).willReturn(queryApi);
        given(queryApi.query(anyString(), eq(HumidityMaxMinDaily.class))).willReturn(Collections.emptyList());

        assertThrows(HumidityNotFoundException.class, () -> humidityService.getWeeklyHumidity());
    }

    @Test
    void getMonthlyHumidity() {
        Instant time = Instant.now();
        float monthlyMaxHumidity = 80.0f;
        float monthlyMinHumidity = 60.0f;
        float dailyMaxHumidity = 80.0f;
        float dailyMinHumidity = 60.0f;
        HumidityMaxMinMonthly humidityMonthly = new HumidityMaxMinMonthly(time, monthlyMaxHumidity, monthlyMinHumidity);
        HumidityMaxMinDaily humidityDaily = new HumidityMaxMinDaily(time, dailyMaxHumidity, dailyMinHumidity);

        given(influxDBClient.getQueryApi()).willReturn(queryApi);
        given(queryApi.query(anyString(), eq(HumidityMaxMinMonthly.class))).willReturn(new ArrayList<>(List.of(humidityMonthly)));
        given(queryApi.query(anyString(), eq(HumidityMaxMinDaily.class))).willReturn(new ArrayList<>(List.of(humidityDaily)));

        List<HumidityMaxMinMonthly> resultHumidity = humidityService.getMonthlyHumidity();

        assertAll(
                () -> assertEquals(humidityMonthly.getTime(), resultHumidity.get(0).getTime()),
                () -> assertEquals(humidityDaily.getTime(), resultHumidity.get(1).getTime()),
                () -> assertEquals(humidityMonthly.getMaxHumidity(), resultHumidity.get(0).getMaxHumidity()),
                () -> assertEquals(humidityMonthly.getMinHumidity(), resultHumidity.get(0).getMinHumidity()),
                () -> assertEquals(humidityDaily.getMaxHumidity(), resultHumidity.get(1).getMaxHumidity()),
                () -> assertEquals(humidityDaily.getMinHumidity(), resultHumidity.get(1).getMinHumidity())
        );
    }

    @Test
    void getMonthlyHumidityException() {
        given(influxDBClient.getQueryApi()).willReturn(queryApi);
        given(queryApi.query(anyString(), eq(HumidityMaxMinDaily.class))).willReturn(Collections.emptyList());

        assertThrows(HumidityNotFoundException.class, () -> humidityService.getMonthlyHumidity());
    }
}
