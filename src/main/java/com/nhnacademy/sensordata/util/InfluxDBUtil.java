package com.nhnacademy.sensordata.util;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.query.dsl.Flux;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static com.influxdb.query.dsl.functions.restriction.Restrictions.*;

@Component
@RequiredArgsConstructor
public class InfluxDBUtil {
    private final InfluxDBClient influxDBClient;
    private static final String BUCKET_NAME = "TxT-iot";
    private static final String ROW_KEY = "_time";
    private static final String COLUMN_KEY = "_field";
    private static final String COLUMN_VALUE = "_value";
    private static final String MAX = "max_";
    private static final String MIN = "min_";

    public <M> Optional<M> getSensorData(String collectionType, Class<M> clazz) {
        Flux fluxQuery = Flux.from(BUCKET_NAME)
                .range(-1L, ChronoUnit.MINUTES)
                .filter(measurement().equal(collectionType))
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
                .query(fluxQuery.toString(), clazz)
                .stream()
                .findFirst();
    }

    public <M> Optional<M> getLastSensorData(String collectionType, Class<M> clazz) {
        Flux fluxQueryHourly = Flux.from(BUCKET_NAME)
                .range(-1L, ChronoUnit.DAYS)
                .filter(measurement().equal(collectionType.concat("_hourly")))
                .last()
                .pivot()
                .withRowKey(new String[]{ROW_KEY})
                .withColumnKey(new String[]{COLUMN_KEY})
                .withValueColumn(COLUMN_VALUE)
                .timeShift(9L, ChronoUnit.HOURS);

        return influxDBClient.getQueryApi()
                .query(fluxQueryHourly.toString(), clazz)
                .stream()
                .findFirst();
    }

    public <M> List<M> getSensorDataList(Instant startTime, Instant endTime, String collectionType, String intervalType, Class<M> clazz) {
        Flux fluxQuery = Flux.from(BUCKET_NAME)
                .range(startTime, endTime)
                .filter(measurement().equal(collectionType.concat(intervalType)))
                .filter(or(
                        field().equal(MAX.concat(collectionType)),
                        field().equal(MIN.concat(collectionType))
                ))
                .pivot()
                .withRowKey(new String[]{ROW_KEY})
                .withColumnKey(new String[]{COLUMN_KEY})
                .withValueColumn(COLUMN_VALUE)
                .timeShift(9L, ChronoUnit.HOURS);

        return influxDBClient.getQueryApi()
                .query(fluxQuery.toString(), clazz);
    }
}
