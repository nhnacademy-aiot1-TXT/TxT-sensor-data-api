package com.nhnacademy.sensordata.service;

import com.nhnacademy.sensordata.entity.humidity.Humidity;
import com.nhnacademy.sensordata.entity.humidity.HumidityMaxMinDaily;
import com.nhnacademy.sensordata.entity.humidity.HumidityMaxMinMonthly;
import com.nhnacademy.sensordata.entity.humidity.HumidityMaxMinWeekly;

import java.util.List;

public interface HumidityService {
    Humidity getHumidity();

    List<HumidityMaxMinDaily> getDailyHumidity();

    List<HumidityMaxMinWeekly> getWeeklyHumidity();

    List<HumidityMaxMinMonthly> getMonthlyHumidity();
}
