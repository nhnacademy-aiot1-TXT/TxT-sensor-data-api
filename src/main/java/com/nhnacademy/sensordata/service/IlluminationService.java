package com.nhnacademy.sensordata.service;

import com.nhnacademy.sensordata.entity.illumination.Illumination;
import com.nhnacademy.sensordata.entity.illumination.IlluminationMaxMinDaily;
import com.nhnacademy.sensordata.entity.illumination.IlluminationMaxMinMonthly;
import com.nhnacademy.sensordata.entity.illumination.IlluminationMaxMinWeekly;

import java.util.List;

/**
 * 조도 서비스 interface
 *
 * @author parksangwon
 * @version 1.0.0
 */
public interface IlluminationService {
    /**
     * 조도 단일 조회 메서드
     *
     * @return 단일 조도
     */
    Illumination getIllumination();

    /**
     * 일간 1시간 주기로 만들어진 조도 리스트 조회 메서드
     *
     * @return 일간 조도 리스트
     */
    List<IlluminationMaxMinDaily> getDailyIlluminations();

    /**
     * 주간 하루 주기로 만들어진 조도 리스트 조회 메서드
     *
     * @return 주간 조도 리스트
     */
    List<IlluminationMaxMinWeekly> getWeeklyIlluminations();

    /**
     * 월간 하루 주기로 만들어진 조도 리스트 조회 메서드
     *
     * @return 월간 조도 리스트
     */
    List<IlluminationMaxMinMonthly> getMonthlyIlluminations();
}
