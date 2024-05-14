package com.nhnacademy.sensordata.service;

import com.nhnacademy.sensordata.measurement.co2.Co2;
import com.nhnacademy.sensordata.measurement.co2.Co2MaxMin;
import com.nhnacademy.sensordata.measurement.co2.Co2Mean;

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
    List<Co2MaxMin> getDailyCo2(String place);

    /**
     * 일별(00시 ~ 현재시간) 평균 co2 list 조회 메서드
     *
     * @param place
     * @return 일별 평균 co2 list
     */
    List<Co2Mean> getDailyMeanCo2(String place);

    /**
     * 주별(일주일간 1일 간격) co2 list 조회 메서드
     *
     * @return 주별 co2 list
     */
    List<Co2MaxMin> getWeeklyCo2();

    /**
     * 월별(한달간 1일 간격) co2 list 조회 메서드
     *
     * @return 월별 co2 list
     */
    List<Co2MaxMin> getMonthlyCo2();
}
