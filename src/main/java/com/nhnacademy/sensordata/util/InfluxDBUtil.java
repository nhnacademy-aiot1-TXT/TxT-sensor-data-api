package com.nhnacademy.sensordata.util;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.query.dsl.Flux;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static com.influxdb.query.dsl.functions.restriction.Restrictions.*;

/**
 * influxDB에 접근하기 위한 util 클래스
 *
 * @author parksangwon
 * @version 1.0.0
 */
@Component
@RequiredArgsConstructor
public class InfluxDBUtil {
    private final InfluxDBClient influxDBClient;
    private static final String BUCKET_NAME = "TxT";
    private static final String ROW_KEY = "_time";
    private static final String COLUMN_KEY = "_field";
    private static final String COLUMN_VALUE = "_value";
    private static final String MAX = "max_";
    private static final String MIN = "min_";

    /**
     * 수집 종류에 대한 단일 조회
     *
     * @param <M>            the type parameter
     * @param collectionType the collection type
     * @param place          장소
     * @param clazz          the clazz
     * @return the sensor data
     */
    public <M> Optional<M> getSensorData(String collectionType, String place, Class<M> clazz) {
        Flux fluxQuery = Flux.from(BUCKET_NAME)
                .range(-1L, ChronoUnit.HOURS)
                .filter(measurement().equal(collectionType))
                .filter(or(
                        field().equal("device"),
                        field().equal("place"),
                        field().equal("topic"),
                        field().equal("value")
                ))
                .pivot()
                .withRowKey(new String[]{ROW_KEY})
                .withColumnKey(new String[]{COLUMN_KEY})
                .withValueColumn(COLUMN_VALUE)
                .filter(column("place").equal(place))
                .last("value")
                .timeShift(9L, ChronoUnit.HOURS);

        return influxDBClient.getQueryApi()
                .query(fluxQuery.toString(), clazz)
                .stream()
                .findFirst();
    }

    /**
     * 수집 종류에 대한 일간 마지막 값 조회
     *
     * @param <M>            the type parameter
     * @param start          the start
     * @param collectionType the collection type
     * @param place          장소
     * @param clazz          the clazz
     * @return the last sensor data
     */
    @Cacheable(
            value = "getLastSensorData",
            key = "#collectionType.concat('-').concat(#start.toString()).concat('-').concat(#place)",
            cacheManager = "cacheManagerHourly",
            unless = "#result == null"
    )
    public <M> Optional<M> getLastSensorData(Instant start, String collectionType, String place, Class<M> clazz) {
        Flux fluxQueryHourly = Flux.from(BUCKET_NAME)
                .range(start.minus(1L, ChronoUnit.DAYS))
                .filter(measurement().equal(collectionType.concat("_hourly").concat("_").concat(place)))
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


    /**
     * 수집 종류에 대한 주간/월간 리스트 조회
     *
     * @param <M>            the type parameter
     * @param startTime      the start time
     * @param endTime        the end time
     * @param collectionType the collection type
     * @param intervalType   the interval type
     * @param place          장소
     * @param clazz          the clazz
     * @return the sensor data list
     */
    @Caching(cacheable = {
            @Cacheable(
                    value = "getSensorDataList",
                    condition = "#intervalType == '_hourly'",
                    key = "#collectionType.concat(#intervalType).concat('-').concat(#startTime.toString()).concat('-').concat(#endTime.toString()).concat('-').concat(#place)",
                    cacheManager = "cacheManagerHourly",
                    unless = "#result.isEmpty()"
            ),
            @Cacheable(
                    value = "getSensorDataList",
                    condition = "#intervalType == '_daily'",
                    key = "#collectionType.concat(#intervalType).concat('-').concat(#startTime.toString()).concat('-').concat(#endTime.toString()).concat('-').concat(#place)",
                    cacheManager = "cacheManagerDaily",
                    unless = "#result.isEmpty()"
            )
    })
    public <M> List<M> getSensorDataList(Instant startTime, Instant endTime, String collectionType, String intervalType, String place, Class<M> clazz) {
        Flux fluxQuery = Flux.from(BUCKET_NAME)
                .range(startTime, endTime)
                .filter(measurement().equal(collectionType.concat(intervalType).concat("_").concat(place)))
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

    /**
     * 수집 종류에 대한 일간 평균 리스트 조회
     *
     * @param startTime      the start time
     * @param endTime        the end time
     * @param collectionType the collection type
     * @param place          장소
     * @param clazz          the clazz
     * @param <M>            the type parameter
     * @return the hourly mean data
     */
    @Cacheable(
            value = "getHourlyMeanData",
            key = "#collectionType.concat('-').concat(#startTime.toString()).concat('-').concat(#endTime.toString()).concat('-').concat(#place)",
            cacheManager = "cacheManagerHourly",
            unless = "#result.isEmpty()"
    )
    public <M> List<M> getHourlyMeanData(Instant startTime, Instant endTime, String collectionType, String place, Class<M> clazz) {
        Flux fluxQuery = Flux.from(BUCKET_NAME)
                .range(startTime, endTime)
                .filter(measurement().equal(collectionType))
                .filter(or(
                        field().equal("device"),
                        field().equal("place"),
                        field().equal("topic"),
                        field().equal("value")
                ))
                .pivot()
                .withRowKey(new String[]{ROW_KEY})
                .withColumnKey(new String[]{COLUMN_KEY})
                .withValueColumn(COLUMN_VALUE)
                .filter(column("place").equal(place))
                .aggregateWindow(1L, ChronoUnit.HOURS, "mean")
                .withColumn("value")
                .timeShift(9L, ChronoUnit.HOURS);

        return influxDBClient.getQueryApi()
                .query(fluxQuery.toString(), clazz);
    }
}
