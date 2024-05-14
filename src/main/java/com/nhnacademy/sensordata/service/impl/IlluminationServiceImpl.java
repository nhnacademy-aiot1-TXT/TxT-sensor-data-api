package com.nhnacademy.sensordata.service.impl;

import com.nhnacademy.sensordata.exception.IlluminationNotFoundException;
import com.nhnacademy.sensordata.measurement.illumination.Illumination;
import com.nhnacademy.sensordata.measurement.illumination.IlluminationMaxMin;
import com.nhnacademy.sensordata.measurement.illumination.IlluminationMean;
import com.nhnacademy.sensordata.service.IlluminationService;
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
 * 조도 서비스 class
 *
 * @author parksangwon
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
public class IlluminationServiceImpl implements IlluminationService {
    private final InfluxDBUtil influxDBUtil;
    private static final String COLLECTION_TYPE = "illumination";
    private static final String MIDNIGHT_UNIX_TIME = "%sT15:00:00Z";

    /**
     * influxdb에서 최신 조도를 조회 후 반환하는 메서드
     *
     * @return 단일조도
     */
    @Override
    public Illumination getIllumination() {
        return influxDBUtil.getSensorData(COLLECTION_TYPE, Illumination.class)
                .orElseThrow(() -> new IlluminationNotFoundException("조도를 찾을 수 없습니다."));
    }

    /**
     * influxdb에서 당일 0시부터 1시간 간격으로 현재까지의 조도를 조회 후 반환하는 메서드
     *
     * @return 일간 조도 리스트
     */
    @Override
    public List<IlluminationMaxMin> getDailyIlluminations(String place) {
        Instant startTime = Instant.parse(String.format(MIDNIGHT_UNIX_TIME, LocalDate.now().minusDays(1)));
        LocalDateTime now = LocalDateTime.now().minusHours(9);
        LocalDateTime end = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), now.getHour(), 0, 1);
        Instant endTime = Instant.ofEpochSecond(end.toEpochSecond(ZoneOffset.UTC));

        List<IlluminationMaxMin> illuminations = influxDBUtil.getSensorDataList(startTime, endTime, COLLECTION_TYPE, "_hourly", IlluminationMaxMin.class);

        return illuminations.isEmpty() ? Collections.emptyList() : illuminations;
    }

    /**
     * influxdb에서 당일 0시부터 1시간 간격으로 현재까지의 조도 평균을 조회 후 반환하는 메서드
     *
     * @return 일간 조도 리스트
     */
    @Override
    public List<IlluminationMean> getDailyIlluminationsMean(String place) {
        Instant startTime = Instant.parse(String.format(MIDNIGHT_UNIX_TIME, LocalDate.now().minusDays(1)));
        LocalDateTime now = LocalDateTime.now().minusHours(9);
        LocalDateTime end = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), now.getHour(), 0, 0);
        Instant endTime = Instant.ofEpochSecond(end.toEpochSecond(ZoneOffset.UTC));

        List<IlluminationMean> illuminations = influxDBUtil.getHourlyMeanData(startTime, endTime, COLLECTION_TYPE, place, IlluminationMean.class);

        return illuminations.isEmpty() ? Collections.emptyList() : illuminations;
    }

    /**
     * influxdb에서 일주일 전 0시부터 하루 간격으로 현재까지의 조도를 조회 후 반환하는 리스트
     *
     * @return 주간 조도 리스트
     */
    @Override
    public List<IlluminationMaxMin> getWeeklyIlluminations() {
        Instant startTime = Instant.parse(String.format(MIDNIGHT_UNIX_TIME, LocalDate.now().minusWeeks(1)));
        LocalDateTime now = LocalDateTime.now().minusHours(9);
        LocalDateTime end = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), now.getHour(), 0, 1);
        Instant endTime = Instant.ofEpochSecond(end.toEpochSecond(ZoneOffset.UTC));

        List<IlluminationMaxMin> illuminations = influxDBUtil.getSensorDataList(startTime, endTime, COLLECTION_TYPE, "_daily", IlluminationMaxMin.class);
        ;
        IlluminationMaxMin illuminationMaxMinDaily = influxDBUtil.getLastSensorData(endTime, COLLECTION_TYPE, IlluminationMaxMin.class)
                .orElseThrow(() -> new IlluminationNotFoundException("조도를 찾을 수 없습니다."));

        if (Objects.nonNull(illuminationMaxMinDaily)) {
            illuminations.add(new IlluminationMaxMin(
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
    public List<IlluminationMaxMin> getMonthlyIlluminations() {
        Instant startTime = Instant.parse(String.format(MIDNIGHT_UNIX_TIME, LocalDate.now().minusMonths(1)));
        LocalDateTime now = LocalDateTime.now().minusHours(9);
        LocalDateTime end = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), now.getHour(), 0, 1);
        Instant endTime = Instant.ofEpochSecond(end.toEpochSecond(ZoneOffset.UTC));

        List<IlluminationMaxMin> illuminations = influxDBUtil.getSensorDataList(startTime, endTime, COLLECTION_TYPE, "_daily", IlluminationMaxMin.class);
        IlluminationMaxMin illuminationMaxMinDaily = influxDBUtil.getLastSensorData(endTime, COLLECTION_TYPE, IlluminationMaxMin.class)
                .orElseThrow(() -> new IlluminationNotFoundException("조도를 찾을 수 없습니다."));

        if (Objects.nonNull(illuminationMaxMinDaily)) {
            illuminations.add(new IlluminationMaxMin(
                    illuminationMaxMinDaily.getTime(),
                    illuminationMaxMinDaily.getMaxIllumination(),
                    illuminationMaxMinDaily.getMinIllumination())
            );
        }

        return illuminations.isEmpty() ? Collections.emptyList() : illuminations;
    }
}
