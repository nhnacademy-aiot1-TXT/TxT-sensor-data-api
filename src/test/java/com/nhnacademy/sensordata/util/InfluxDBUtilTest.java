package com.nhnacademy.sensordata.util;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.nhnacademy.sensordata.measurement.temperature.Temperature;
import com.nhnacademy.sensordata.measurement.temperature.TemperatureMaxMin;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@WebMvcTest(InfluxDBUtil.class)
class InfluxDBUtilTest {
    @Autowired
    private InfluxDBUtil influxDBUtil;
    @MockBean
    private InfluxDBClient influxDBClient;
    @Mock
    private QueryApi queryApi;

    @Test
    void getSensorData() {
        Instant time = Instant.now();
        String device = "test device";
        String place = "test place";
        String topic = "test topic";
        Float value = 25.0f;
        Temperature temperature = new Temperature(time, device, place, topic, value);

        given(influxDBClient.getQueryApi()).willReturn(queryApi);
        given(queryApi.query(anyString(), eq(Temperature.class))).willReturn(List.of(temperature));

        Optional<Temperature> temperatureOptional = influxDBUtil.getSensorData("temperature", place, Temperature.class);
        Temperature result = temperatureOptional.orElseThrow();

        assertAll(
                () -> assertNotNull(temperatureOptional),
                () -> assertNotNull(result),
                () -> assertEquals(time, result.getTime()),
                () -> assertEquals(device, result.getDevice()),
                () -> assertEquals(place, result.getPlace()),
                () -> assertEquals(topic, result.getTopic()),
                () -> assertEquals(value, result.getValue())
        );
    }

    @Test
    void getLastSensorData() {
        Instant time = Instant.now();
        Float maxValue = 25.0f;
        Float minValue = 20.0f;
        TemperatureMaxMin temperatureMaxMinDaily = new TemperatureMaxMin(time, maxValue, minValue);

        given(influxDBClient.getQueryApi()).willReturn(queryApi);
        given(queryApi.query(anyString(), eq(TemperatureMaxMin.class))).willReturn(List.of(temperatureMaxMinDaily));

        Optional<TemperatureMaxMin> temperatureOptional = influxDBUtil.getLastSensorData(Instant.now(), "temperature", TemperatureMaxMin.class);
        TemperatureMaxMin result = temperatureOptional.orElseThrow();

        assertAll(
                () -> assertNotNull(temperatureOptional),
                () -> assertNotNull(result),
                () -> assertEquals(time, result.getTime()),
                () -> assertEquals(maxValue, result.getMaxTemperature()),
                () -> assertEquals(minValue, result.getMinTemperature())
        );
    }

    @Test
    void getSensorDataList() {
        Instant time = Instant.now();
        Float maxValue = 25.0f;
        Float minValue = 20.0f;
        TemperatureMaxMin temperatureMaxMinDaily = new TemperatureMaxMin(time, maxValue, minValue);

        given(influxDBClient.getQueryApi()).willReturn(queryApi);
        given(queryApi.query(anyString(), eq(TemperatureMaxMin.class))).willReturn(List.of(temperatureMaxMinDaily));

        List<TemperatureMaxMin> temperatureMaxMinDailies = influxDBUtil.getSensorDataList(Instant.now().minus(1L, ChronoUnit.DAYS), Instant.now(), "temperature", "_daily", TemperatureMaxMin.class);
        TemperatureMaxMin result = temperatureMaxMinDailies.get(0);

        assertAll(
                () -> assertNotNull(temperatureMaxMinDailies),
                () -> assertNotNull(result),
                () -> assertEquals(time, result.getTime()),
                () -> assertEquals(maxValue, result.getMaxTemperature()),
                () -> assertEquals(minValue, result.getMinTemperature())
        );
    }
}