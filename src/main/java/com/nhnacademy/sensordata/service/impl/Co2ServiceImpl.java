package com.nhnacademy.sensordata.service.impl;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.query.dsl.Flux;
import com.nhnacademy.sensordata.entity.co2.Co2;
import com.nhnacademy.sensordata.entity.co2.Co2MaxMinDaily;
import com.nhnacademy.sensordata.entity.co2.Co2MaxMinMonthly;
import com.nhnacademy.sensordata.entity.co2.Co2MaxMinWeekly;
import com.nhnacademy.sensordata.service.Co2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.influxdb.query.dsl.functions.restriction.Restrictions.*;

@Service
@RequiredArgsConstructor
public class Co2ServiceImpl implements Co2Service {
    private final InfluxDBClient influxDBClient;

    @Override
    public Co2 getCo2() {
        Flux fluxQuery = Flux.from("TxT-iot")
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
                .withRowKey(new String[]{"_time"})
                .withColumnKey(new String[]{"_field"})
                .withValueColumn("_value")
                .map("({ r with value: float(v: r.value)})")
                .timeShift(9L, ChronoUnit.HOURS);

        return influxDBClient.getQueryApi().query(fluxQuery.toString(), Co2.class).stream().findFirst().orElse(null);
    }

    @Override
    public List<Co2MaxMinDaily> getDailyCo2() {
        Instant startTime = Instant.parse(String.format("%sT15:00:00Z", LocalDate.now().minusDays(1)));
        Instant endTime = Instant.now();

        Flux query = Flux.from("TxT-iot-old")
                .range(startTime, endTime)
                .filter(measurement().equal("co2_hourly"))
                .filter(or(
                        field().equal("max_co2"),
                        field().equal("min_co2")
                ))
                .pivot()
                .withRowKey(new String[]{"_time"})
                .withColumnKey(new String[]{"_field"})
                .withValueColumn("_value")
                .timeShift(9L, ChronoUnit.HOURS);

        return influxDBClient.getQueryApi().query(query.toString(), Co2MaxMinDaily.class);
    }

    @Override
    public List<Co2MaxMinWeekly> getWeeklyCo2() {
        Instant startTime = Instant.parse(String.format("%sT15:00:00Z", LocalDate.now().minusWeeks(1)));
        Instant endTime = Instant.now();

        Flux query = Flux.from("TxT-iot-old")
                .range(startTime, endTime)
                .filter(measurement().equal("co2_daily"))
                .filter(or(
                        field().equal("max_co2"),
                        field().equal("min_co2")
                ))
                .pivot()
                .withRowKey(new String[]{"_time"})
                .withColumnKey(new String[]{"_field"})
                .withValueColumn("_value")
                .timeShift(9L, ChronoUnit.HOURS);
        Flux lastHourQuery = Flux.from("TxT-iot-old")
                .range(-1L, ChronoUnit.DAYS)
                .filter(measurement().equal("co2_hourly"))
                .last()
                .pivot()
                .withRowKey(new String[]{"_time"})
                .withColumnKey(new String[]{"_field"})
                .withValueColumn("_value")
                .timeShift(9L, ChronoUnit.HOURS);

        List<Co2MaxMinWeekly> weeklyList = influxDBClient.getQueryApi().query(query.toString(), Co2MaxMinWeekly.class);
        Co2MaxMinWeekly lastHour = influxDBClient.getQueryApi().query(lastHourQuery.toString(), Co2MaxMinWeekly.class).get(0);
        weeklyList.add(new Co2MaxMinWeekly(lastHour.getTime(), lastHour.getMaxCo2(), lastHour.getMinCo2()));
        return weeklyList;
    }

    @Override
    public List<Co2MaxMinMonthly> getMonthlyCo2() {
        Instant startTime = Instant.parse(String.format("%sT15:00:00Z", LocalDate.now().minusWeeks(1)));
        Instant endTime = Instant.now();

        Flux query = Flux.from("TxT-iot-old")
                .range(startTime, endTime)
                .filter(measurement().equal("co2_daily"))
                .filter(or(
                        field().equal("max_co2"),
                        field().equal("min_co2")
                ))
                .pivot()
                .withRowKey(new String[]{"_time"})
                .withColumnKey(new String[]{"_field"})
                .withValueColumn("_value")
                .timeShift(9L, ChronoUnit.HOURS);

        Flux lastHourQuery = Flux.from("TxT-iot-old")
                .range(-1L, ChronoUnit.DAYS)
                .filter(measurement().equal("co2_hourly"))
                .last()
                .pivot()
                .withRowKey(new String[]{"_time"})
                .withColumnKey(new String[]{"_field"})
                .withValueColumn("_value")
                .timeShift(9L, ChronoUnit.HOURS);

        List<Co2MaxMinMonthly> monthlyList = influxDBClient.getQueryApi().query(query.toString(), Co2MaxMinMonthly.class);
        Co2MaxMinDaily lastHour = influxDBClient.getQueryApi().query(lastHourQuery.toString(), Co2MaxMinDaily.class).get(0);
        monthlyList.add(new Co2MaxMinMonthly(lastHour.getTime(), lastHour.getMaxCo2(), lastHour.getMinCo2()));
        return monthlyList;
    }
}
