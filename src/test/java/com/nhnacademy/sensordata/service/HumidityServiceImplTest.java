package com.nhnacademy.sensordata.service;

import com.nhnacademy.sensordata.exception.HumidityNotFoundException;
import com.nhnacademy.sensordata.measurement.humidity.Humidity;
import com.nhnacademy.sensordata.measurement.humidity.HumidityMaxMin;
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
class HumidityServiceImplTest {
    @Autowired
    private HumidityService humidityService;
    @MockBean
    private InfluxDBUtil influxDBUtil;

    @Test
    void getHumidity() {
        Instant time = Instant.now();
        String device = "test device";
        String place = "test place";
        String topic = "test topic";
        float value = 20.0f;
        Humidity humidity = new Humidity(time, device, place, topic, value);

        given(influxDBUtil.getSensorData(anyString(), anyString(), eq(Humidity.class))).willReturn(Optional.of(humidity));

        Humidity resultHumidity = humidityService.getHumidity(place);

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
        String place = "test place";

        given(influxDBUtil.getSensorData(anyString(), anyString(), eq(Humidity.class))).willReturn(Optional.empty());

        assertThrows(HumidityNotFoundException.class, () -> humidityService.getHumidity(place));
    }

    @Test
    void getDailyHumidity() {
        Instant time = Instant.now();
        float maxHumidity = 80.0f;
        float minHumidity = 60.0f;
        HumidityMaxMin humidityDaily = new HumidityMaxMin(time, maxHumidity, minHumidity);

        given(influxDBUtil.getSensorDataList(any(), any(), anyString(), anyString(), eq(HumidityMaxMin.class))).willReturn(List.of(humidityDaily));

        HumidityMaxMin resultHumidity = humidityService.getDailyHumidity().get(0);

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
        HumidityMaxMin humidityWeekly = new HumidityMaxMin(time, weeklyMaxHumidity, weeklyMinHumidity);
        HumidityMaxMin humidityDaily = new HumidityMaxMin(time, dailyMaxHumidity, dailyMinHumidity);

        given(influxDBUtil.getSensorDataList(any(), any(), anyString(), anyString(), eq(HumidityMaxMin.class))).willReturn(new ArrayList<>(List.of(humidityWeekly)));
        given(influxDBUtil.getLastSensorData(any(), anyString(), eq(HumidityMaxMin.class))).willReturn(Optional.of(humidityDaily));

        List<HumidityMaxMin> resultHumidity = humidityService.getWeeklyHumidity();

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
        given(influxDBUtil.getLastSensorData(any(), anyString(), eq(HumidityMaxMin.class))).willReturn(Optional.empty());

        assertThrows(HumidityNotFoundException.class, () -> humidityService.getWeeklyHumidity());
    }

    @Test
    void getMonthlyHumidity() {
        Instant time = Instant.now();
        float monthlyMaxHumidity = 80.0f;
        float monthlyMinHumidity = 60.0f;
        float dailyMaxHumidity = 80.0f;
        float dailyMinHumidity = 60.0f;
        HumidityMaxMin humidityMonthly = new HumidityMaxMin(time, monthlyMaxHumidity, monthlyMinHumidity);
        HumidityMaxMin humidityDaily = new HumidityMaxMin(time, dailyMaxHumidity, dailyMinHumidity);

        given(influxDBUtil.getSensorDataList(any(), any(), anyString(), anyString(), eq(HumidityMaxMin.class))).willReturn(new ArrayList<>(List.of(humidityMonthly)));
        given(influxDBUtil.getLastSensorData(any(), anyString(), eq(HumidityMaxMin.class))).willReturn(Optional.of(humidityDaily));

        List<HumidityMaxMin> resultHumidity = humidityService.getMonthlyHumidity();

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
        given(influxDBUtil.getLastSensorData(any(), anyString(), eq(HumidityMaxMin.class))).willReturn(Optional.empty());

        assertThrows(HumidityNotFoundException.class, () -> humidityService.getMonthlyHumidity());
    }
}
