package com.nhnacademy.sensordata.service.impl;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.query.dsl.Flux;
import com.nhnacademy.sensordata.entity.voc.Voc;
import com.nhnacademy.sensordata.service.VocService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;

import static com.influxdb.query.dsl.functions.restriction.Restrictions.*;

/**
 * voc 서비스 class
 *
 * @author parksangwon
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
public class VocServiceImpl implements VocService {
    private final InfluxDBClient influxDBClient;
    private static final String BUCKET_NAME = "TxT-iot";
    private static final String ROW_KEY = "_time";
    private static final String COLUMN_KEY = "_field";
    private static final String COLUMN_VALUE = "_value";

    /**
     * influxdb에서 최신 voc를 조회 후 반환하는 메서드
     *
     * @return 단일 온도
     */
    @Override
    public Voc getVoc() {
        Flux fluxQuery = Flux.from(BUCKET_NAME)
                .range(-1L, ChronoUnit.DAYS)
                .filter(measurement().equal("tvoc"))
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
                .query(fluxQuery.toString(), Voc.class)
                .stream()
                .findFirst()
                .orElse(null);
    }
}
