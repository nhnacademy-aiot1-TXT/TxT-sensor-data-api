package com.nhnacademy.sensordata.service.impl;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.query.dsl.Flux;
import com.nhnacademy.sensordata.measurement.illumination.Illumination;
import com.nhnacademy.sensordata.measurement.illumination.IlluminationMaxMinDaily;
import com.nhnacademy.sensordata.measurement.illumination.IlluminationMaxMinMonthly;
import com.nhnacademy.sensordata.measurement.illumination.IlluminationMaxMinWeekly;
import com.nhnacademy.sensordata.exception.IlluminationNotFoundException;
import com.nhnacademy.sensordata.service.IlluminationService;
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
 * 조도 서비스 class
 *
 * @author parksangwon
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
public class IlluminationServiceImpl implements IlluminationService {
    private final InfluxDBClient influxDBClient;
    private static final String BUCKET_NAME = "TxT-iot";
    private static final String ROW_KEY = "_time";
    private static final String COLUMN_KEY = "_field";
    private static final String COLUMN_VALUE = "_value";

    /**
     * influxdb에서 최신 조도를 조회 후 반환하는 메서드
     *
     * @return 단일조도
     */
    @Override
    public Illumination getIllumination() {
        Flux fluxQuery = Flux.from(BUCKET_NAME)
                .range(-1L, ChronoUnit.DAYS)
                .filter(measurement().equal("illumination"))
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
                .timeShift(9L, ChronoUnit.HOURS);

        return influxDBClient.getQueryApi()
                .query(fluxQuery.toString(), Illumination.class)
                .stream()
                .findFirst()
                .orElseThrow(() -> new IlluminationNotFoundException("조도를 찾을 수 없습니다."));
    }

    /**
     * influxdb에서 당일 0시부터 1시간 간격으로 현재까지의 조도를 조회 후 반환하는 메서드
     *
     * @return 일간 조도 리스트
     */
    @Override
    public List<IlluminationMaxMinDaily> getDailyIlluminations() {
        Instant startTime = Instant.parse(String.format("%sT15:00:00Z", LocalDate.now().minusDays(1)));
        Instant endTime = Instant.now();
        Flux fluxQuery = Flux.from(BUCKET_NAME)
                .range(startTime, endTime)
                .filter(measurement().equal("illumination_hourly"))
                .filter(or(
                        field().equal("max_illumination"),
                        field().equal("min_illumination")
                ))
                .pivot()
                .withRowKey(new String[]{ROW_KEY})
                .withColumnKey(new String[]{COLUMN_KEY})
                .withValueColumn(COLUMN_VALUE)
                .timeShift(9L, ChronoUnit.HOURS);

        List<IlluminationMaxMinDaily> illuminations = influxDBClient.getQueryApi().query(fluxQuery.toString(), IlluminationMaxMinDaily.class);

        return illuminations.isEmpty() ? Collections.emptyList() : illuminations;
    }

    /**
     * influxdb에서 일주일 전 0시부터 하루 간격으로 현재까지의 조도를 조회 후 반환하는 리스트
     *
     * @return 주간 조도 리스트
     */
    @Override
    public List<IlluminationMaxMinWeekly> getWeeklyIlluminations() {
        Instant startTime = Instant.parse(String.format("%sT15:00:00Z", LocalDate.now().minusWeeks(1)));
        Instant endTime = Instant.now();
        Flux fluxQueryDaily = Flux.from(BUCKET_NAME)
                .range(startTime, endTime)
                .filter(measurement().equal("illumination_daily"))
                .filter(or(
                        field().equal("max_illumination"),
                        field().equal("min_illumination")
                ))
                .pivot()
                .withRowKey(new String[]{ROW_KEY})
                .withColumnKey(new String[]{COLUMN_KEY})
                .withValueColumn(COLUMN_VALUE)
                .timeShift(9L, ChronoUnit.HOURS);

        Flux fluxQueryHourly = Flux.from(BUCKET_NAME)
                .range(-1L, ChronoUnit.DAYS)
                .filter(measurement().equal("illumination_hourly"))
                .last()
                .pivot()
                .withRowKey(new String[]{ROW_KEY})
                .withColumnKey(new String[]{COLUMN_KEY})
                .withValueColumn(COLUMN_VALUE)
                .timeShift(9L, ChronoUnit.HOURS);


        List<IlluminationMaxMinWeekly> illuminations = influxDBClient.getQueryApi()
                .query(fluxQueryDaily.toString(), IlluminationMaxMinWeekly.class);
        IlluminationMaxMinDaily illuminationMaxMinDaily = influxDBClient.getQueryApi()
                .query(fluxQueryHourly.toString(), IlluminationMaxMinDaily.class)
                .stream()
                .findFirst()
                .orElseThrow(() -> new IlluminationNotFoundException("조도를 찾을 수 없습니다."));

        if (Objects.nonNull(illuminationMaxMinDaily)) {
            illuminations.add(new IlluminationMaxMinWeekly(
                    illuminationMaxMinDaily.getTime(),
                    illuminationMaxMinDaily.getMaxIllumination(),
                    illuminationMaxMinDaily.getMinIllumination())
            );
        }

        return illuminations.isEmpty() ? Collections.emptyList() : illuminations;
    }

    /**
     * influxdb에서 한달 전 0시부터 하루 간격으로 현재까지의 조도를 조회 후 반환하는 리스트
     *
     * @return 월간 조도 리스트
     */
    @Override
    public List<IlluminationMaxMinMonthly> getMonthlyIlluminations() {
        Instant startTime = Instant.parse(String.format("%sT15:00:00Z", LocalDate.now().minusMonths(1)));
        Instant endTime = Instant.now();
        Flux fluxQueryDaily = Flux.from(BUCKET_NAME)
                .range(startTime, endTime)
                .filter(measurement().equal("illumination_daily"))
                .filter(or(
                        field().equal("max_illumination"),
                        field().equal("min_illumination")
                ))
                .pivot()
                .withRowKey(new String[]{ROW_KEY})
                .withColumnKey(new String[]{COLUMN_KEY})
                .withValueColumn(COLUMN_VALUE)
                .timeShift(9L, ChronoUnit.HOURS);

        Flux fluxQueryHourly = Flux.from(BUCKET_NAME)
                .range(-1L, ChronoUnit.DAYS)
                .filter(measurement().equal("illumination_hourly"))
                .last()
                .pivot()
                .withRowKey(new String[]{ROW_KEY})
                .withColumnKey(new String[]{COLUMN_KEY})
                .withValueColumn(COLUMN_VALUE)
                .timeShift(9L, ChronoUnit.HOURS);


        List<IlluminationMaxMinMonthly> illuminations = influxDBClient.getQueryApi()
                .query(fluxQueryDaily.toString(), IlluminationMaxMinMonthly.class);
        IlluminationMaxMinDaily illuminationMaxMinDaily = influxDBClient.getQueryApi()
                .query(fluxQueryHourly.toString(), IlluminationMaxMinDaily.class)
                .stream()
                .findFirst()
                .orElseThrow(() -> new IlluminationNotFoundException("조도를 찾을 수 없습니다."));

        if (Objects.nonNull(illuminationMaxMinDaily)) {
            illuminations.add(new IlluminationMaxMinMonthly(
                    illuminationMaxMinDaily.getTime(),
                    illuminationMaxMinDaily.getMaxIllumination(),
                    illuminationMaxMinDaily.getMinIllumination())
            );
        }

        return illuminations.isEmpty() ? Collections.emptyList() : illuminations;
    }
}
