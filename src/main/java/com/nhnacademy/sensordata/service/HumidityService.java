package com.nhnacademy.sensordata.service;

import com.nhnacademy.sensordata.measurement.humidity.Humidity;
import com.nhnacademy.sensordata.measurement.humidity.HumidityMaxMin;
import com.nhnacademy.sensordata.measurement.humidity.HumidityMean;

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
    Humidity getHumidity(String place);

    /**
     * 일별(00시 ~ 현재시간) humidity list 조회 메서드
     *
     * @return 일별 humidity list
     */
    List<HumidityMaxMin> getDailyHumidity(String place);

    /**
     * 일별(00시 ~ 현재시간) 평균 humidity list 조회 메서드
     *
     * @param place
     * @return 일별 평균 humidity list
     */
    List<HumidityMean> getDailyMeanHumidity(String place);

    /**
     * 주별(일주일간 1일 간격) humidity list 조회 메서드
     *
     * @return 주별 humidity list
     */
    List<HumidityMaxMin> getWeeklyHumidity(String place);

    /**
     * 월별(한달간 1일 간격) humidity list 조회 메서드
     *
     * @return 월별 humidity list
     */
    List<HumidityMaxMin> getMonthlyHumidity(String place);
}
