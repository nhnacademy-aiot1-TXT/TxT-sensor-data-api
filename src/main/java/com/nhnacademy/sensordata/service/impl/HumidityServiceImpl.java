package com.nhnacademy.sensordata.service.impl;

import com.nhnacademy.sensordata.exception.HumidityNotFoundException;
import com.nhnacademy.sensordata.measurement.humidity.Humidity;
import com.nhnacademy.sensordata.measurement.humidity.HumidityMaxMin;
import com.nhnacademy.sensordata.measurement.humidity.HumidityMean;
import com.nhnacademy.sensordata.service.HumidityService;
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
 * humidity service class
 *
 * @author jongsikk
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
public class HumidityServiceImpl implements HumidityService {
    private final InfluxDBUtil influxDBUtil;
    private static final String COLLECTION_TYPE = "humidity";
    private static final String MIDNIGHT_UNIX_TIME = "%sT15:00:00Z";
    private static final String HUMIDITY_NOT_FOUND_MESSAGE = "습도 정보를 찾을 수 없습니다.";

    /**
     * 가장 최신 humidity 조회 메서드
     *
     * @param place 장소
     * @return 단일 humidity
     */
    @Override
    public Humidity getHumidity(String place) {
        return influxDBUtil.getSensorData(COLLECTION_TYPE, place, Humidity.class)
                .orElseThrow(() -> new HumidityNotFoundException(HUMIDITY_NOT_FOUND_MESSAGE));
    }

    /**
     * 일별(00시 ~ 현재시간) 1시간 간격 humidity list
     *
     * @param place 장소
     * @return 일별 humidity list
     */
    @Override
    public List<HumidityMaxMin> getDailyHumidity(String place) {
        Instant startTime = Instant.parse(String.format(MIDNIGHT_UNIX_TIME, LocalDate.now().minusDays(1)));
        LocalDateTime now = LocalDateTime.now().minusHours(9);
        LocalDateTime end = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), now.getHour(), 0, 1);
        Instant endTime = Instant.ofEpochSecond(end.toEpochSecond(ZoneOffset.UTC));

        List<HumidityMaxMin> dailyList = influxDBUtil.getSensorDataList(startTime, endTime, COLLECTION_TYPE, "_hourly", place, HumidityMaxMin.class);

        return dailyList.isEmpty() ? Collections.emptyList() : dailyList;
    }

    /**
     * 일별(00시 ~ 현재시간) 1시간 간격 평균 humidity list
     *
     * @param place 장소
     * @return 일별 평균 humidity list
     */
    @Override
    public List<HumidityMean> getDailyMeanHumidity(String place) {
        Instant startTime = Instant.parse(String.format(MIDNIGHT_UNIX_TIME, LocalDate.now().minusDays(1)));
        LocalDateTime now = LocalDateTime.now().minusHours(9);
        LocalDateTime end = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), now.getHour(), 0, 0);
        Instant endTime = Instant.ofEpochSecond(end.toEpochSecond(ZoneOffset.UTC));

        List<HumidityMean> humidityList = influxDBUtil.getHourlyMeanData(startTime, endTime, COLLECTION_TYPE, place, HumidityMean.class);

        return humidityList.isEmpty() ? Collections.emptyList() : humidityList;
    }

    /**
     * 주별(일주일간 1일 간격) humidity list
     *
     * @param place 장소
     * @return 주별 humidity list
     */
    @Override
    public List<HumidityMaxMin> getWeeklyHumidity(String place) {
        Instant startTime = Instant.parse(String.format(MIDNIGHT_UNIX_TIME, LocalDate.now().minusWeeks(1)));
        LocalDateTime now = LocalDateTime.now().minusHours(9);
        LocalDateTime end = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), now.getHour(), 0, 1);
        Instant endTime = Instant.ofEpochSecond(end.toEpochSecond(ZoneOffset.UTC));

        List<HumidityMaxMin> weeklyList = influxDBUtil.getSensorDataList(startTime, endTime, COLLECTION_TYPE, "_daily", place, HumidityMaxMin.class);

        HumidityMaxMin lastHour = influxDBUtil.getLastSensorData(endTime, COLLECTION_TYPE, place, HumidityMaxMin.class)
                .orElseThrow(() -> new HumidityNotFoundException(HUMIDITY_NOT_FOUND_MESSAGE));

        if (Objects.nonNull(lastHour)) {
            weeklyList.add(new HumidityMaxMin(lastHour.getTime(), lastHour.getMaxHumidity(), lastHour.getMinHumidity()));
        }

        return weeklyList.isEmpty() ? Collections.emptyList() : weeklyList;
    }

    /**
     * 월별(한달간 1일 간격) humidity list
     *
     * @param place 장소
     * @return 월별 humidity list
     */
    @Override
    public List<HumidityMaxMin> getMonthlyHumidity(String place) {
        Instant startTime = Instant.parse(String.format(MIDNIGHT_UNIX_TIME, LocalDate.now().minusMonths(1)));
        LocalDateTime now = LocalDateTime.now().minusHours(9);
        LocalDateTime end = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), now.getHour(), 0, 1);
        Instant endTime = Instant.ofEpochSecond(end.toEpochSecond(ZoneOffset.UTC));

        List<HumidityMaxMin> monthlyList = influxDBUtil.getSensorDataList(startTime, endTime, COLLECTION_TYPE, "_daily", place, HumidityMaxMin.class);

        HumidityMaxMin lastHour = influxDBUtil.getLastSensorData(endTime, COLLECTION_TYPE, place, HumidityMaxMin.class)
                .orElseThrow(() -> new HumidityNotFoundException(HUMIDITY_NOT_FOUND_MESSAGE));

        if (Objects.nonNull(lastHour)) {
            monthlyList.add(new HumidityMaxMin(lastHour.getTime(), lastHour.getMaxHumidity(), lastHour.getMinHumidity()));
        }

        return monthlyList.isEmpty() ? Collections.emptyList() : monthlyList;
    }
}
