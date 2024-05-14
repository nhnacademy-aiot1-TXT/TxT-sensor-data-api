package com.nhnacademy.sensordata.service.impl;

import com.nhnacademy.sensordata.exception.TemperatureNotFoundException;
import com.nhnacademy.sensordata.measurement.temperature.Temperature;
import com.nhnacademy.sensordata.measurement.temperature.TemperatureMaxMin;
import com.nhnacademy.sensordata.measurement.temperature.TemperatureMean;
import com.nhnacademy.sensordata.service.TemperatureService;
import com.nhnacademy.sensordata.util.InfluxDBUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 온도 서비스 class
 *
 * @author parksangwon
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
public class TemperatureServiceImpl implements TemperatureService {
    private final InfluxDBUtil influxDBUtil;
    private static final String COLLECTION_TYPE = "temperature";
    private static final String MIDNIGHT_UNIX_TIME = "%sT15:00:00Z";

    /**
     * influxdb에서 최신 온도를 조회 후 반환하는 메서드
     *
     * @return 단일 온도
     */
    @Override
    public Temperature getTemperature(String place) {
        return influxDBUtil.getSensorData(COLLECTION_TYPE, place, Temperature.class)
                .orElseThrow(() -> new TemperatureNotFoundException("온도를 찾을 수 없습니다."));
    }

    /**
     * influxdb에서 당일 0시부터 1시간 간격으로 현재까지의 온도를 조회 후 반환하는 메서드
     *
     * @return 일간 온도 리스트
     */
    @Override
    public List<TemperatureMaxMin> getDailyTemperatures() {
        Instant startTime = Instant.parse(String.format(MIDNIGHT_UNIX_TIME, LocalDate.now().minusDays(1)));
        LocalDateTime now = LocalDateTime.now().minusHours(9);
        LocalDateTime end = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), now.getHour(), 0, 1);
        Instant endTime = Instant.ofEpochSecond(end.toEpochSecond(ZoneOffset.UTC));

        List<TemperatureMaxMin> temperatures = influxDBUtil.getSensorDataList(startTime, endTime, COLLECTION_TYPE, "_hourly", TemperatureMaxMin.class);

        return temperatures.isEmpty() ? Collections.emptyList() : temperatures;
    }

    /**
     * influxdb에서 당일 0시부터 1시간 간격으로 현재까지의 온도 평균을 조회 후 반환하는 메서드
     *
     * @return 일간 온도 리스트
     */
    @Override
    public List<TemperatureMean> getDailyTemperaturesMean(String place) {
        Instant startTime = Instant.parse(String.format(MIDNIGHT_UNIX_TIME, LocalDate.now().minusDays(1)));
        LocalDateTime now = LocalDateTime.now().minusHours(9);
        LocalDateTime end = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), now.getHour(), 0, 0);
        Instant endTime = Instant.ofEpochSecond(end.toEpochSecond(ZoneOffset.UTC));

        List<TemperatureMean> temperatures = influxDBUtil.getHourlyMeanData(startTime, endTime, COLLECTION_TYPE, place, TemperatureMean.class);

        return temperatures.isEmpty() ? Collections.emptyList() : temperatures;
    }

    /**
     * influxdb에서 일주일 전 0시부터 하루 간격으로 현재까지의 온도를 조회 후 반환하는 리스트
     *
     * @return 주간 온도 리스트
     */
    @Override
    public List<TemperatureMaxMin> getWeeklyTemperatures() {
        Instant startTime = Instant.parse(String.format(MIDNIGHT_UNIX_TIME, LocalDate.now().minusWeeks(1)));
        LocalDateTime now = LocalDateTime.now().minusHours(9);
        LocalDateTime end = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), now.getHour(), 0, 1);
        Instant endTime = Instant.ofEpochSecond(end.toEpochSecond(ZoneOffset.UTC));

        List<TemperatureMaxMin> temperatures = influxDBUtil.getSensorDataList(startTime, endTime, COLLECTION_TYPE, "_daily", TemperatureMaxMin.class);
        TemperatureMaxMin temperatureMaxMinDaily = influxDBUtil.getLastSensorData(endTime, COLLECTION_TYPE, TemperatureMaxMin.class)
                .orElseThrow(() -> new TemperatureNotFoundException("온도를 찾을 수 없습니다."));

        if (Objects.nonNull(temperatureMaxMinDaily)) {
            temperatures.add(new TemperatureMaxMin(
                    temperatureMaxMinDaily.getTime(),
                    temperatureMaxMinDaily.getMaxTemperature(),
                    temperatureMaxMinDaily.getMinTemperature())
            );
        }

        return temperatures.isEmpty() ? Collections.emptyList() : temperatures;
    }

    /**
     * influxdb에서 한달 전 0시부터 하루 간격으로 현재까지의 온도를 조회 후 반환하는 리스트
     *
     * @return 월간 온도 리스트
     */
    @Override
    public List<TemperatureMaxMin> getMonthlyTemperatures() {
        Instant startTime = Instant.parse(String.format(MIDNIGHT_UNIX_TIME, LocalDate.now().minusMonths(1)));
        LocalDateTime now = LocalDateTime.now().minusHours(9);
        LocalDateTime end = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), now.getHour(), 0, 1);
        Instant endTime = Instant.ofEpochSecond(end.toEpochSecond(ZoneOffset.UTC));

        List<TemperatureMaxMin> temperatures = influxDBUtil.getSensorDataList(startTime, endTime, COLLECTION_TYPE, "_daily", TemperatureMaxMin.class);
        TemperatureMaxMin temperatureMaxMinDaily = influxDBUtil.getLastSensorData(endTime, COLLECTION_TYPE, TemperatureMaxMin.class)
                .orElseThrow(() -> new TemperatureNotFoundException("온도를 찾을 수 없습니다."));

        if (Objects.nonNull(temperatureMaxMinDaily)) {
            temperatures.add(new TemperatureMaxMin(
                    temperatureMaxMinDaily.getTime(),
                    temperatureMaxMinDaily.getMaxTemperature(),
                    temperatureMaxMinDaily.getMinTemperature())
            );
        }

        return temperatures.isEmpty() ? Collections.emptyList() : temperatures;
    }
}
