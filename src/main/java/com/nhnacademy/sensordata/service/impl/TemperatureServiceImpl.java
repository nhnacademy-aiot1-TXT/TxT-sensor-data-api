package com.nhnacademy.sensordata.service.impl;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.query.dsl.Flux;
import com.nhnacademy.sensordata.entity.temperature.Temperature;
import com.nhnacademy.sensordata.entity.temperature.TemperatureMaxMinDaily;
import com.nhnacademy.sensordata.entity.temperature.TemperatureMaxMinMonthly;
import com.nhnacademy.sensordata.entity.temperature.TemperatureMaxMinWeekly;
import com.nhnacademy.sensordata.service.TemperatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.influxdb.query.dsl.functions.restriction.Restrictions.*;

@Service
@RequiredArgsConstructor
public class TemperatureServiceImpl implements TemperatureService {
    private final InfluxDBClient influxDBClient;

    @Override
    public Temperature getTemperature() {
        Flux fluxQuery = Flux.from("TxT-iot")
                .range(-1L, ChronoUnit.MINUTES)
                .filter(measurement().equal("temperature"))
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
                .timeShift(9L, ChronoUnit.HOURS);

        return influxDBClient.getQueryApi()
                .query(fluxQuery.toString(), Temperature.class)
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<TemperatureMaxMinDaily> getDailyTemperatures() {
        Instant startTime = Instant.parse(String.format("%sT15:00:00Z", LocalDate.now().minusDays(1)));
        Instant endTime = Instant.now();
        Flux fluxQuery = Flux.from("TxT-iot-old")
                .range(startTime, endTime)
                .filter(measurement().equal("temperature_hourly"))
                .filter(or(
                        field().equal("max_temperature"),
                        field().equal("min_temperature")
                ))
                .pivot()
                .withRowKey(new String[]{"_time"})
                .withColumnKey(new String[]{"_field"})
                .withValueColumn("_value")
                .timeShift(9L, ChronoUnit.HOURS);

        List<TemperatureMaxMinDaily> temperatures = influxDBClient.getQueryApi().query(fluxQuery.toString(), TemperatureMaxMinDaily.class);

        return temperatures.isEmpty() ? Collections.emptyList() : temperatures;
    }

    @Override
    public List<TemperatureMaxMinWeekly> getWeeklyTemperatures() {
        Instant startTime = Instant.parse(String.format("%sT15:00:00Z", LocalDate.now().minusWeeks(1)));
        Instant endTime = Instant.now();
        Flux fluxQueryDaily = Flux.from("TxT-iot-old")
                .range(startTime, endTime)
                .filter(measurement().equal("temperature_daily"))
                .filter(or(
                        field().equal("max_temperature"),
                        field().equal("min_temperature")
                ))
                .pivot()
                .withRowKey(new String[]{"_time"})
                .withColumnKey(new String[]{"_field"})
                .withValueColumn("_value")
                .timeShift(9L, ChronoUnit.HOURS);

        Flux fluxQueryHourly = Flux.from("TxT-iot-old")
                .range(-1L, ChronoUnit.DAYS)
                .filter(measurement().equal("temperature_hourly"))
                .last()
                .pivot()
                .withRowKey(new String[]{"_time"})
                .withColumnKey(new String[]{"_field"})
                .withValueColumn("_value")
                .timeShift(9L, ChronoUnit.HOURS);


        List<TemperatureMaxMinWeekly> temperatures = influxDBClient.getQueryApi()
                .query(fluxQueryDaily.toString(), TemperatureMaxMinWeekly.class);
        TemperatureMaxMinDaily temperatureMaxMinDaily = influxDBClient.getQueryApi()
                .query(fluxQueryHourly.toString(), TemperatureMaxMinDaily.class)
                .stream()
                .findFirst()
                .orElse(null);

        if (Objects.nonNull(temperatureMaxMinDaily)) {
            temperatures.add(new TemperatureMaxMinWeekly(
                    temperatureMaxMinDaily.getTime(),
                    temperatureMaxMinDaily.getMaxTemperature(),
                    temperatureMaxMinDaily.getMinTemperature())
            );
        }

        return temperatures.isEmpty() ? Collections.emptyList() : temperatures;
    }

    @Override
    public List<TemperatureMaxMinMonthly> getMonthlyTemperatures() {
        Instant startTime = Instant.parse(String.format("%sT15:00:00Z", LocalDate.now().minusMonths(1)));
        Instant endTime = Instant.now();
        Flux fluxQueryDaily = Flux.from("TxT-iot-old")
                .range(startTime, endTime)
                .filter(measurement().equal("temperature_daily"))
                .filter(or(
                        field().equal("max_temperature"),
                        field().equal("min_temperature")
                ))
                .pivot()
                .withRowKey(new String[]{"_time"})
                .withColumnKey(new String[]{"_field"})
                .withValueColumn("_value")
                .timeShift(9L, ChronoUnit.HOURS);

        Flux fluxQueryHourly = Flux.from("TxT-iot-old")
                .range(-1L, ChronoUnit.DAYS)
                .filter(measurement().equal("temperature_hourly"))
                .last()
                .pivot()
                .withRowKey(new String[]{"_time"})
                .withColumnKey(new String[]{"_field"})
                .withValueColumn("_value")
                .timeShift(9L, ChronoUnit.HOURS);


        List<TemperatureMaxMinMonthly> temperatures = influxDBClient.getQueryApi()
                .query(fluxQueryDaily.toString(), TemperatureMaxMinMonthly.class);
        TemperatureMaxMinDaily temperatureMaxMinDaily = influxDBClient.getQueryApi()
                .query(fluxQueryHourly.toString(), TemperatureMaxMinDaily.class)
                .stream()
                .findFirst()
                .orElse(null);

        if (Objects.nonNull(temperatureMaxMinDaily)) {
            temperatures.add(new TemperatureMaxMinMonthly(
                    temperatureMaxMinDaily.getTime(),
                    temperatureMaxMinDaily.getMaxTemperature(),
                    temperatureMaxMinDaily.getMinTemperature())
            );
        }

        return temperatures.isEmpty() ? Collections.emptyList() : temperatures;
    }
}
