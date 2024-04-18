package com.nhnacademy.sensordata.service.impl;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.query.dsl.Flux;
import com.nhnacademy.sensordata.entity.co2.Co2;
import com.nhnacademy.sensordata.entity.co2.Co2MaxMinDaily;
import com.nhnacademy.sensordata.entity.co2.Co2MaxMinMonthly;
import com.nhnacademy.sensordata.entity.co2.Co2MaxMinWeekly;
import com.nhnacademy.sensordata.exception.Co2NotFoundException;
import com.nhnacademy.sensordata.service.Co2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.influxdb.query.dsl.functions.restriction.Restrictions.*;

/**
 * co2 service class
 *
 * @author jongsikk
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
public class Co2ServiceImpl implements Co2Service {
    private final InfluxDBClient influxDBClient;
    private static final String BUCKET_NAME = "TxT-iot";
    private static final String ROW_KEY = "_time";
    private static final String COLUMN_KEY = "_field";
    private static final String COLUMN_VALUE = "_value";

    /**
     * 가장 최신 co2 조회 메서드
     *
     * @return 단일 co2
     */
    @Override
    public Co2 getCo2() {
        Flux fluxQuery = Flux.from(BUCKET_NAME)
                .range(-1L, ChronoUnit.MINUTES)
                .filter(measurement().equal("co2"))
                .filter(or(
                        field().equal("device"),
                        field().equal("place"),
                        field().equal("topic"),
                        field().equal("value")
                ))
                .last()
                .pivot()
                .withRowKey(new String[]{ROW_KEY})
                .withColumnKey(new String[]{COLUMN_KEY})
                .withValueColumn(COLUMN_VALUE)
                .map("({ r with value: float(v: r.value)})")
                .timeShift(9L, ChronoUnit.HOURS);

        return influxDBClient.getQueryApi()
                .query(fluxQuery.toString(), Co2.class)
                .stream()
                .findFirst()
                .orElseThrow(() -> new Co2NotFoundException("Co2 정보를 찾을 수 없습니다"));
    }

    /**
     * 일별(00시 ~ 현재시간) 1시간 간격 co2 list
     *
     * @return 일별 co2 list
     */
    @Override
    public List<Co2MaxMinDaily> getDailyCo2() {
        Instant startTime = Instant.parse(String.format("%sT15:00:00Z", LocalDate.now().minusDays(1)));
        Instant endTime = Instant.now();

        Flux query = Flux.from(BUCKET_NAME)
                .range(startTime, endTime)
                .filter(measurement().equal("co2_hourly"))
                .filter(or(
                        field().equal("max_co2"),
                        field().equal("min_co2")
                ))
                .pivot()
                .withRowKey(new String[]{ROW_KEY})
                .withColumnKey(new String[]{COLUMN_KEY})
                .withValueColumn(COLUMN_VALUE)
                .timeShift(9L, ChronoUnit.HOURS);

        return influxDBClient.getQueryApi().query(query.toString(), Co2MaxMinDaily.class);
    }

    /**
     * 주별(일주일간 1일 간격) co2 list
     *
     * @return 주별 co2 list
     */
    @Override
    public List<Co2MaxMinWeekly> getWeeklyCo2() {
        Instant startTime = Instant.parse(String.format("%sT15:00:00Z", LocalDate.now().minusWeeks(1)));
        Instant endTime = Instant.now();

        Flux query = Flux.from(BUCKET_NAME)
                .range(startTime, endTime)
                .filter(measurement().equal("co2_daily"))
                .filter(or(
                        field().equal("max_co2"),
                        field().equal("min_co2")
                ))
                .pivot()
                .withRowKey(new String[]{ROW_KEY})
                .withColumnKey(new String[]{COLUMN_KEY})
                .withValueColumn(COLUMN_VALUE)
                .timeShift(9L, ChronoUnit.HOURS);

        Flux lastHourQuery = Flux.from(BUCKET_NAME)
                .range(-1L, ChronoUnit.DAYS)
                .filter(measurement().equal("co2_hourly"))
                .last()
                .pivot()
                .withRowKey(new String[]{ROW_KEY})
                .withColumnKey(new String[]{COLUMN_KEY})
                .withValueColumn(COLUMN_VALUE)
                .timeShift(9L, ChronoUnit.HOURS);

        List<Co2MaxMinWeekly> weeklyList = influxDBClient.getQueryApi()
                .query(query.toString(), Co2MaxMinWeekly.class);

        Co2MaxMinWeekly lastHour = influxDBClient.getQueryApi()
                .query(lastHourQuery.toString(), Co2MaxMinWeekly.class)
                .stream()
                .findFirst()
                .orElseThrow(() -> new Co2NotFoundException("Co2 정보를 찾을 수 없습니다"));

        if (Objects.nonNull(lastHour)) {
            weeklyList.add(new Co2MaxMinWeekly(lastHour.getTime(), lastHour.getMaxCo2(), lastHour.getMinCo2()));
        }

        return weeklyList.isEmpty() ? Collections.emptyList() : weeklyList;
    }

    /**
     * 월별(한달간 1일 간격) co2 list
     *
     * @return 월별 co2 list
     */
    @Override
    public List<Co2MaxMinMonthly> getMonthlyCo2() {
        Instant startTime = Instant.parse(String.format("%sT15:00:00Z", LocalDate.now().minusWeeks(1)));
        Instant endTime = Instant.now();

        Flux query = Flux.from(BUCKET_NAME)
                .range(startTime, endTime)
                .filter(measurement().equal("co2_daily"))
                .filter(or(
                        field().equal("max_co2"),
                        field().equal("min_co2")
                ))
                .pivot()
                .withRowKey(new String[]{ROW_KEY})
                .withColumnKey(new String[]{COLUMN_KEY})
                .withValueColumn(COLUMN_VALUE)
                .timeShift(9L, ChronoUnit.HOURS);

        Flux lastHourQuery = Flux.from(BUCKET_NAME)
                .range(-1L, ChronoUnit.DAYS)
                .filter(measurement().equal("co2_hourly"))
                .last()
                .pivot()
                .withRowKey(new String[]{ROW_KEY})
                .withColumnKey(new String[]{COLUMN_KEY})
                .withValueColumn(COLUMN_VALUE)
                .timeShift(9L, ChronoUnit.HOURS);

        List<Co2MaxMinMonthly> monthlyList = influxDBClient.getQueryApi()
                .query(query.toString(), Co2MaxMinMonthly.class);

        Co2MaxMinDaily lastHour = influxDBClient.getQueryApi()
                .query(lastHourQuery.toString(), Co2MaxMinDaily.class)
                .stream()
                .findFirst()
                .orElseThrow(() -> new Co2NotFoundException("Co2 정보를 찾을 수 없습니다"));

        if (Objects.nonNull(lastHour)) {
            monthlyList.add(new Co2MaxMinMonthly(lastHour.getTime(), lastHour.getMaxCo2(), lastHour.getMinCo2()));
        }

        return monthlyList.isEmpty() ? Collections.emptyList() : monthlyList;
    }
}
