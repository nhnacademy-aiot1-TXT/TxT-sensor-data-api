package com.nhnacademy.sensordata.service.impl;

import com.nhnacademy.sensordata.exception.Co2NotFoundException;
import com.nhnacademy.sensordata.measurement.co2.Co2;
import com.nhnacademy.sensordata.measurement.co2.Co2MaxMin;
import com.nhnacademy.sensordata.service.Co2Service;
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
 * co2 service class
 *
 * @author jongsikk
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
public class Co2ServiceImpl implements Co2Service {
    private final InfluxDBUtil influxDBUtil;
    private static final String COLLECTION_TYPE = "co2";
    private static final String MIDNIGHT_UNIX_TIME = "%sT15:00:00Z";

    /**
     * 가장 최신 co2 조회 메서드
     *
     * @return 단일 co2
     */
    @Override
    public Co2 getCo2() {
        return influxDBUtil.getSensorData(COLLECTION_TYPE, Co2.class)
                .orElseThrow(() -> new Co2NotFoundException("Co2 정보를 찾을 수 없습니다"));
    }

    /**
     * 일별(00시 ~ 현재시간) 1시간 간격 co2 list
     *
     * @return 일별 co2 list
     */
    @Override
    public List<Co2MaxMin> getDailyCo2() {
        Instant startTime = Instant.parse(String.format(MIDNIGHT_UNIX_TIME, LocalDate.now().minusDays(1)));
        LocalDateTime now = LocalDateTime.now().minusHours(9);
        LocalDateTime end = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), now.getHour(), 0, 1);
        Instant endTime = Instant.ofEpochSecond(end.toEpochSecond(ZoneOffset.UTC));
        List<Co2MaxMin> co2List = influxDBUtil.getSensorDataList(startTime, endTime, COLLECTION_TYPE, "_hourly", Co2MaxMin.class);

        return co2List.isEmpty() ? Collections.emptyList() : co2List;
    }

    /**
     * 주별(일주일간 1일 간격) co2 list
     *
     * @return 주별 co2 list
     */
    @Override
    public List<Co2MaxMin> getWeeklyCo2() {
        Instant startTime = Instant.parse(String.format(MIDNIGHT_UNIX_TIME, LocalDate.now().minusWeeks(1)));
        LocalDateTime now = LocalDateTime.now().minusHours(9);
        LocalDateTime end = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), now.getHour(), 0, 1);
        Instant endTime = Instant.ofEpochSecond(end.toEpochSecond(ZoneOffset.UTC));


        List<Co2MaxMin> weeklyList = influxDBUtil.getSensorDataList(startTime, endTime, COLLECTION_TYPE, "_daily", Co2MaxMin.class);

        Co2MaxMin lastHour = influxDBUtil.getLastSensorData(COLLECTION_TYPE, Co2MaxMin.class)
                .orElseThrow(() -> new Co2NotFoundException("Co2 정보를 찾을 수 없습니다"));

        if (Objects.nonNull(lastHour)) {
            weeklyList.add(new Co2MaxMin(lastHour.getTime(), lastHour.getMaxCo2(), lastHour.getMinCo2()));
        }

        return weeklyList.isEmpty() ? Collections.emptyList() : weeklyList;
    }

    /**
     * 월별(한달간 1일 간격) co2 list
     *
     * @return 월별 co2 list
     */
    @Override
    public List<Co2MaxMin> getMonthlyCo2() {
        Instant startTime = Instant.parse(String.format(MIDNIGHT_UNIX_TIME, LocalDate.now().minusMonths(1)));
        LocalDateTime now = LocalDateTime.now().minusHours(9);
        LocalDateTime end = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), now.getHour(), 0, 1);
        Instant endTime = Instant.ofEpochSecond(end.toEpochSecond(ZoneOffset.UTC));

        List<Co2MaxMin> monthlyList = influxDBUtil.getSensorDataList(startTime, endTime, COLLECTION_TYPE, "_daily", Co2MaxMin.class);

        Co2MaxMin lastHour = influxDBUtil.getLastSensorData(COLLECTION_TYPE, Co2MaxMin.class)
                .orElseThrow(() -> new Co2NotFoundException("Co2 정보를 찾을 수 없습니다"));

        if (Objects.nonNull(lastHour)) {
            monthlyList.add(new Co2MaxMin(lastHour.getTime(), lastHour.getMaxCo2(), lastHour.getMinCo2()));
        }

        return monthlyList.isEmpty() ? Collections.emptyList() : monthlyList;
    }
}
