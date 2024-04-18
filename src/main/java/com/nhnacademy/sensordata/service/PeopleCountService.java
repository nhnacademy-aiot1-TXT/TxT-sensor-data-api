package com.nhnacademy.sensordata.service;

import com.nhnacademy.sensordata.entity.people_count.PeopleCount;

/**
 * people-count service interface
 *
 * @author jongsikk
 * @version 1.0.0
 */
public interface PeopleCountService {
    /**
     * people-count 단일 조회 메서드
     *
     * @return 단일 people-count
     */
    PeopleCount getPeopleCount();
}
