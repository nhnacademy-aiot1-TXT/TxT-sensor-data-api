package com.nhnacademy.sensordata.service;

import com.nhnacademy.sensordata.measurement.humidity.Humidity;
import com.nhnacademy.sensordata.measurement.humidity.HumidityMaxMinDaily;
import com.nhnacademy.sensordata.measurement.humidity.HumidityMaxMinMonthly;
import com.nhnacademy.sensordata.measurement.humidity.HumidityMaxMinWeekly;

import java.util.List;

/**
 * humidity service interface
 *
 * @author jongsikk
 * @version 1.0.0
 */
public interface HumidityService {
    /**
     * humidity 단일 조회 메서드
     *
     * @return 단일 humidity
     */
    Humidity getHumidity();

    /**
     * 일별(00시 ~ 현재시간) humidity list 조회 메서드
     *
     * @return 일별 humidity list
     */
    List<HumidityMaxMinDaily> getDailyHumidity();

    /**
     * 주별(일주일간 1일 간격) humidity list 조회 메서드
     *
     * @return 주별 humidity list
     */
    List<HumidityMaxMinWeekly> getWeeklyHumidity();

    /**
     * 월별(한달간 1일 간격) humidity list 조회 메서드
     *
     * @return 월별 humidity list
     */
    List<HumidityMaxMinMonthly> getMonthlyHumidity();
}
