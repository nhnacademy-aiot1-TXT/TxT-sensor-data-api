package com.nhnacademy.sensordata.service.impl;

import com.nhnacademy.sensordata.exception.HumidityNotFoundException;
import com.nhnacademy.sensordata.measurement.humidity.Humidity;
import com.nhnacademy.sensordata.measurement.humidity.HumidityMaxMinDaily;
import com.nhnacademy.sensordata.measurement.humidity.HumidityMaxMinMonthly;
import com.nhnacademy.sensordata.measurement.humidity.HumidityMaxMinWeekly;
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

    /**
     * 가장 최신 humidity 조회 메서드
     *
     * @return 단일 humidity
     */
    @Override
    public Humidity getHumidity() {
        return influxDBUtil.getSensorData(COLLECTION_TYPE, Humidity.class)
                .orElseThrow(() -> new HumidityNotFoundException("습도 정보를 찾을 수 없습니다."));
    }

    /**
     * 일별(00시 ~ 현재시간) 1시간 간격 humidity list
     *
     * @return 일별 humidity list
     */
    @Override
    public List<HumidityMaxMinDaily> getDailyHumidity() {
        Instant startTime = Instant.parse(String.format(MIDNIGHT_UNIX_TIME, LocalDate.now().minusDays(1)));
        LocalDateTime now = LocalDateTime.now().minusHours(9);
        LocalDateTime end = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), now.getHour(), 0, 1);
        Instant endTime = Instant.ofEpochSecond(end.toEpochSecond(ZoneOffset.UTC));

        List<HumidityMaxMinDaily> dailyList = influxDBUtil.getSensorDataList(startTime, endTime, COLLECTION_TYPE, "_hourly", HumidityMaxMinDaily.class);

        return dailyList.isEmpty() ? Collections.emptyList() : dailyList;
    }

    /**
     * 주별(일주일간 1일 간격) humidity list
     *
     * @return 주별 humidity list
     */
    @Override
    public List<HumidityMaxMinWeekly> getWeeklyHumidity() {
        Instant startTime = Instant.parse(String.format(MIDNIGHT_UNIX_TIME, LocalDate.now().minusWeeks(1)));
        LocalDateTime now = LocalDateTime.now().minusHours(9);
        LocalDateTime end = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), now.getHour(), 0, 1);
        Instant endTime = Instant.ofEpochSecond(end.toEpochSecond(ZoneOffset.UTC));

        List<HumidityMaxMinWeekly> weeklyList = influxDBUtil.getSensorDataList(startTime, endTime, COLLECTION_TYPE, "_daily", HumidityMaxMinWeekly.class);

        HumidityMaxMinDaily lastHour = influxDBUtil.getLastSensorData(COLLECTION_TYPE, HumidityMaxMinDaily.class)
                .orElseThrow(() -> new HumidityNotFoundException("습도 정보를 찾을 수 없습니다."));

        if (Objects.nonNull(lastHour)) {
            weeklyList.add(new HumidityMaxMinWeekly(lastHour.getTime(), lastHour.getMaxHumidity(), lastHour.getMinHumidity()));
        }

        return weeklyList.isEmpty() ? Collections.emptyList() : weeklyList;
    }

    /**
     * 월별(한달간 1일 간격) humidity list
     *
     * @return 월별 humidity list
     */
    @Override
    public List<HumidityMaxMinMonthly> getMonthlyHumidity() {
        Instant startTime = Instant.parse(String.format(MIDNIGHT_UNIX_TIME, LocalDate.now().minusMonths(1)));
        LocalDateTime now = LocalDateTime.now().minusHours(9);
        LocalDateTime end = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), now.getHour(), 0, 1);
        Instant endTime = Instant.ofEpochSecond(end.toEpochSecond(ZoneOffset.UTC));

        List<HumidityMaxMinMonthly> monthlyList = influxDBUtil.getSensorDataList(startTime, endTime, COLLECTION_TYPE, "_daily", HumidityMaxMinMonthly.class);

        HumidityMaxMinDaily lastHour = influxDBUtil.getLastSensorData(COLLECTION_TYPE, HumidityMaxMinDaily.class)
                .orElseThrow(() -> new HumidityNotFoundException("습도 정보를 찾을 수 없습니다."));

        if (Objects.nonNull(lastHour)) {
            monthlyList.add(new HumidityMaxMinMonthly(lastHour.getTime(), lastHour.getMaxHumidity(), lastHour.getMinHumidity()));
        }

        return monthlyList.isEmpty() ? Collections.emptyList() : monthlyList;
    }
}
