package com.nhnacademy.sensordata.service.impl;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.query.dsl.Flux;
import com.nhnacademy.sensordata.measurement.people_count.PeopleCount;
import com.nhnacademy.sensordata.exception.PeopleCountNotFoundException;
import com.nhnacademy.sensordata.service.PeopleCountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.Map;

import static com.influxdb.query.dsl.functions.restriction.Restrictions.field;
import static com.influxdb.query.dsl.functions.restriction.Restrictions.measurement;

/**
 * people-count service class
 *
 * @author jongsikk
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
public class PeopleCountServiceImpl implements PeopleCountService {
    private final InfluxDBClient influxDBClient;
    private static final String BUCKET_NAME = "TxT-iot";
    private static final String ROW_KEY = "_time";

    /**
     * 가장 최신 people-count 조회 메서드
     *
     * @return 단일 people-count
     */
    @Override
    public PeopleCount getPeopleCount() {
        Flux inCount = Flux.from(BUCKET_NAME)
                .range(-5L, ChronoUnit.MINUTES)
                .filter(measurement().equal("total_in_count"))
                .filter(field().equal("value"))
                .map("({ _time: r._time, _value: r._value})")
                .aggregateWindow(1L, ChronoUnit.MINUTES, "last")
                .timeShift(9L, ChronoUnit.HOURS);

        Flux outCount = Flux.from(BUCKET_NAME)
                .range(-5L, ChronoUnit.MINUTES)
                .filter(measurement().equal("total_out_count"))
                .filter(field().equal("value"))
                .map("({ _time: r._time, _value: r._value})")
                .aggregateWindow(1L, ChronoUnit.MINUTES, "last")
                .timeShift(9L, ChronoUnit.HOURS);

        Flux joinQuery = Flux.join()
                .withTable("inCount", inCount)
                .withTable("outCount", outCount)
                .withOn(ROW_KEY)
                .rename(Map.of("_value_inCount", "total_in_count", "_value_outCount", "total_out_count"));

        return influxDBClient.getQueryApi()
                .query(joinQuery.toString(), PeopleCount.class)
                .stream()
                .findFirst()
                .orElseThrow(() -> new PeopleCountNotFoundException("People Count 정보를 찾을 수 없습니다."));
    }
}
