package com.nhnacademy.sensordata.service.impl;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.query.dsl.Flux;
import com.nhnacademy.sensordata.exception.PeopleCountNotFoundException;
import com.nhnacademy.sensordata.measurement.people_count.PeopleCount;
import com.nhnacademy.sensordata.service.PeopleCountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;

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
        Flux countQuery = Flux.from(BUCKET_NAME)
                .range(-1L, ChronoUnit.MINUTES)
                .filter(measurement().equal("total_people_count"))
                .filter(field().equal("value"))
                .last()
                .timeShift(9L, ChronoUnit.HOURS);

        return influxDBClient.getQueryApi()
                .query(countQuery.toString(), PeopleCount.class)
                .stream()
                .findFirst()
                .orElseThrow(() -> new PeopleCountNotFoundException("People Count 정보를 찾을 수 없습니다."));
    }
}
