package com.nhnacademy.sensordata.service.impl;

import com.nhnacademy.sensordata.exception.PeopleCountNotFoundException;
import com.nhnacademy.sensordata.measurement.people_count.PeopleCount;
import com.nhnacademy.sensordata.service.PeopleCountService;
import com.nhnacademy.sensordata.util.InfluxDBUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * people-count service class
 *
 * @author jongsikk
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
public class PeopleCountServiceImpl implements PeopleCountService {
    private final InfluxDBUtil influxDBUtil;
    private static final String COLLECTION_TYPE = "total_people_count";
    private static final String PLACE = "class_a";
    private static final String PEOPLE_COUNT_NOT_FOUND_MESSAGE = "People Count 정보를 찾을 수 없습니다.";

    /**
     * 가장 최신 people-count 조회 메서드
     *
     * @return 단일 people-count
     */
    @Override
    public PeopleCount getPeopleCount() {
        return influxDBUtil.getSensorData(COLLECTION_TYPE, PLACE, PeopleCount.class)
                .orElseThrow(() -> new PeopleCountNotFoundException(PEOPLE_COUNT_NOT_FOUND_MESSAGE));
    }
}
