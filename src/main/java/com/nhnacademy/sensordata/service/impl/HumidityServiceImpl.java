package com.nhnacademy.sensordata.service.impl;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.query.dsl.Flux;
import com.nhnacademy.sensordata.entity.humidity.Humidity;
import com.nhnacademy.sensordata.entity.humidity.HumidityMaxMinDaily;
import com.nhnacademy.sensordata.entity.humidity.HumidityMaxMinMonthly;
import com.nhnacademy.sensordata.entity.humidity.HumidityMaxMinWeekly;
import com.nhnacademy.sensordata.service.HumidityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.influxdb.query.dsl.functions.restriction.Restrictions.*;

@Service
@RequiredArgsConstructor
public class HumidityServiceImpl implements HumidityService {
    private final InfluxDBClient influxDBClient;
    private static final String BUCKET_NAME = "TxT-iot";
    private static final String ROW_KEY = "_time";
    private static final String COLUMN_KEY = "_field";
    private static final String COLUMN_VALUE = "_value";

    @Override
    public Humidity getHumidity() {
        Flux fluxQuery = Flux.from(BUCKET_NAME)
                .range(-1L, ChronoUnit.MINUTES)
                .filter(measurement().equal("humidity"))
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

        return influxDBClient.getQueryApi().query(fluxQuery.toString(), Humidity.class).stream().findFirst().orElse(null);
    }

    @Override
    public List<HumidityMaxMinDaily> getDailyHumidity() {
        Instant startTime = Instant.parse(String.format("%sT15:00:00Z", LocalDate.now().minusDays(1)));
        Instant endTime = Instant.now();

        Flux query = Flux.from(BUCKET_NAME)
                .range(startTime, endTime)
                .filter(measurement().equal("humidity_hourly"))
                .filter(or(
                        field().equal("max_humidity"),
                        field().equal("min_humidity")
                ))
                .pivot()
                .withRowKey(new String[]{ROW_KEY})
                .withColumnKey(new String[]{COLUMN_KEY})
                .withValueColumn(COLUMN_VALUE)
                .timeShift(9L, ChronoUnit.HOURS);

        return influxDBClient.getQueryApi().query(query.toString(), HumidityMaxMinDaily.class);
    }

    @Override
    public List<HumidityMaxMinWeekly> getWeeklyHumidity() {
        Instant startTime = Instant.parse(String.format("%sT15:00:00Z", LocalDate.now().minusWeeks(1)));
        Instant endTime = Instant.now();

        Flux query = Flux.from(BUCKET_NAME)
                .range(startTime, endTime)
                .filter(measurement().equal("humidity_daily"))
                .filter(or(
                        field().equal("max_humidity"),
                        field().equal("min_humidity")
                ))
                .pivot()
                .withRowKey(new String[]{ROW_KEY})
                .withColumnKey(new String[]{COLUMN_KEY})
                .withValueColumn(COLUMN_VALUE)
                .timeShift(9L, ChronoUnit.HOURS);
        Flux lastHourQuery = Flux.from(BUCKET_NAME)
                .range(-1L, ChronoUnit.DAYS)
                .filter(measurement().equal("humidity_hourly"))
                .last()
                .pivot()
                .withRowKey(new String[]{ROW_KEY})
                .withColumnKey(new String[]{COLUMN_KEY})
                .withValueColumn(COLUMN_VALUE)
                .timeShift(9L, ChronoUnit.HOURS);

        List<HumidityMaxMinWeekly> weeklyList = influxDBClient.getQueryApi().query(query.toString(), HumidityMaxMinWeekly.class);
        HumidityMaxMinDaily lastHour = influxDBClient.getQueryApi().query(lastHourQuery.toString(), HumidityMaxMinDaily.class).get(0);
        weeklyList.add(new HumidityMaxMinWeekly(lastHour.getTime(), lastHour.getMaxHumidity(), lastHour.getMinHumidity()));
        return weeklyList;
    }

    @Override
    public List<HumidityMaxMinMonthly> getMonthlyHumidity() {
        Instant startTime = Instant.parse(String.format("%sT15:00:00Z", LocalDate.now().minusWeeks(1)));
        Instant endTime = Instant.now();

        Flux query = Flux.from(BUCKET_NAME)
                .range(startTime, endTime)
                .filter(measurement().equal("humidity_daily"))
                .filter(or(
                        field().equal("max_humidity"),
                        field().equal("min_humidity")
                ))
                .pivot()
                .withRowKey(new String[]{ROW_KEY})
                .withColumnKey(new String[]{COLUMN_KEY})
                .withValueColumn(COLUMN_VALUE)
                .timeShift(9L, ChronoUnit.HOURS);

        Flux lastHourQuery = Flux.from(BUCKET_NAME)
                .range(-1L, ChronoUnit.DAYS)
                .filter(measurement().equal("humidity_hourly"))
                .last()
                .pivot()
                .withRowKey(new String[]{ROW_KEY})
                .withColumnKey(new String[]{COLUMN_KEY})
                .withValueColumn(COLUMN_VALUE)
                .timeShift(9L, ChronoUnit.HOURS);

        List<HumidityMaxMinMonthly> monthlyList = influxDBClient.getQueryApi().query(query.toString(), HumidityMaxMinMonthly.class);
        HumidityMaxMinDaily lastHour = influxDBClient.getQueryApi().query(lastHourQuery.toString(), HumidityMaxMinDaily.class).get(0);
        monthlyList.add(new HumidityMaxMinMonthly(lastHour.getTime(), lastHour.getMaxHumidity(), lastHour.getMinHumidity()));
        return monthlyList;
    }
}
