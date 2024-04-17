package com.nhnacademy.sensordata.service;

import com.nhnacademy.sensordata.entity.co2.Co2;
import com.nhnacademy.sensordata.entity.co2.Co2MaxMinDaily;
import com.nhnacademy.sensordata.entity.co2.Co2MaxMinMonthly;
import com.nhnacademy.sensordata.entity.co2.Co2MaxMinWeekly;

import java.util.List;

/**
 * co2 service interface
 *
 * @author jongsikk
 * @version 1.0.0
 */
public interface Co2Service {
    /**
     * co2 단일 조회 메서드
     *
     * @return 단일 co2
     */
    Co2 getCo2();

    /**
     * 일별(00시 ~ 현재시간) co2 list 조회 메서드
     *
     * @return 일별 co2 list
     */
    List<Co2MaxMinDaily> getDailyCo2();

    /**
     * 주별(일주일간 1일 간격) co2 list 조회 메서드
     *
     * @return 주별 co2 list
     */
    List<Co2MaxMinWeekly> getWeeklyCo2();

    /**
     * 월별(한달간 1일 간격) co2 list 조회 메서드
     *
     * @return 월별 co2 list
     */
    List<Co2MaxMinMonthly> getMonthlyCo2();
}
