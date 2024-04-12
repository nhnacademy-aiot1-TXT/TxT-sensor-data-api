package com.nhnacademy.sensordata.service.impl;

import com.nhnacademy.sensordata.entity.co2.Co2;
import com.nhnacademy.sensordata.entity.co2.Co2MaxMinDaily;
import com.nhnacademy.sensordata.entity.co2.Co2MaxMinMonthly;
import com.nhnacademy.sensordata.entity.co2.Co2MaxMinWeekly;
import com.nhnacademy.sensordata.service.Co2Service;
import com.nhnacademy.sensordata.utils.InfluxDBUtil;
import lombok.RequiredArgsConstructor;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Co2ServiceImpl implements Co2Service {
    private final InfluxDBResultMapper resultMapper;
    private final InfluxDBUtil influxDBUtil;

    @Override
    public Co2 getCo2() {
        QueryResult queryResult = influxDBUtil.processingQuery("select time, device, place, topic, value from co2 order by time desc limit 1");

        Co2 co2 = resultMapper.toPOJO(queryResult, Co2.class).get(0);

        if (Objects.nonNull(co2)) {
            co2.setTime(co2.getTime().plus(9, ChronoUnit.HOURS));
        }

        return co2;
    }

    @Override
    public List<Co2MaxMinDaily> getDailyCo2() {
        LocalDate today = LocalDate.now();

        QueryResult queryResult = influxDBUtil.processingQuery(String.format("select * from co2_hourly where time >= '%sT15:00:00Z' AND time < '%sT15:00:00Z'", today.minusDays(1), today));

        List<Co2MaxMinDaily> co2MaxMinList = resultMapper.toPOJO(queryResult, Co2MaxMinDaily.class);

        co2MaxMinList = co2MaxMinList.stream()
                .map(co2 -> new Co2MaxMinDaily(
                                co2.getTime().plus(9, ChronoUnit.HOURS),
                                co2.getMaxCo2(),
                                co2.getMinCo2()
                        )
                )
                .collect(Collectors.toList());

        return co2MaxMinList.isEmpty() ? Collections.emptyList() : co2MaxMinList;
    }

    @Override
    public List<Co2MaxMinWeekly> getWeeklyCo2() {
        LocalDate today = LocalDate.now();

        QueryResult queryResult = influxDBUtil.processingQuery(String.format("select * from co2_daily where time >= '%sT15:00:00Z' AND time < '%sT15:00:00Z'", today.minusWeeks(1), today));
        QueryResult queryResult2 = influxDBUtil.processingQuery("select * from co2_hourly order by time desc");

        List<Co2MaxMinWeekly> co2MaxMinList = resultMapper.toPOJO(queryResult, Co2MaxMinWeekly.class);
        Co2MaxMinDaily co2LastHour = resultMapper.toPOJO(queryResult2, Co2MaxMinDaily.class).get(0);

        co2MaxMinList = co2MaxMinList.stream()
                .map(co2 -> new Co2MaxMinWeekly(
                        co2.getTime().plus(9, ChronoUnit.HOURS),
                        co2.getMaxCo2(),
                        co2.getMinCo2()
                ))
                .collect(Collectors.toList());

        if (Objects.nonNull(co2LastHour)) {
            co2MaxMinList.add(new Co2MaxMinWeekly(
                    co2LastHour.getTime().plus(9, ChronoUnit.HOURS),
                    co2LastHour.getMaxCo2(),
                    co2LastHour.getMinCo2()));
        }

        return co2MaxMinList.isEmpty() ? Collections.emptyList() : co2MaxMinList;
    }

    @Override
    public List<Co2MaxMinMonthly> getMonthlyCo2() {
        LocalDate today = LocalDate.now();

        QueryResult queryResult = influxDBUtil.processingQuery(String.format("select * from co2_daily where time >= '%sT15:00:00Z' AND time < '%sT15:00:00Z'", today.minusWeeks(1), today));
        QueryResult queryResult2 = influxDBUtil.processingQuery("select * from co2_hourly order by time desc");

        List<Co2MaxMinMonthly> co2MaxMinList = resultMapper.toPOJO(queryResult, Co2MaxMinMonthly.class);
        Co2MaxMinDaily co2LastHour = resultMapper.toPOJO(queryResult2, Co2MaxMinDaily.class).get(0);

        co2MaxMinList = co2MaxMinList.stream()
                .map(co2 -> new Co2MaxMinMonthly(
                        co2.getTime().plus(9, ChronoUnit.HOURS),
                        co2.getMaxCo2(),
                        co2.getMinCo2()
                ))
                .collect(Collectors.toList());

        if (Objects.nonNull(co2LastHour)) {
            co2MaxMinList.add(new Co2MaxMinMonthly(
                    co2LastHour.getTime().plus(9, ChronoUnit.HOURS),
                    co2LastHour.getMaxCo2(),
                    co2LastHour.getMinCo2()));
        }

        return co2MaxMinList.isEmpty() ? Collections.emptyList() : co2MaxMinList;
    }
}
