package com.nhnacademy.sensordata.service;

import com.nhnacademy.sensordata.entity.Humidity;
import com.nhnacademy.sensordata.entity.HumidityMaxMinDaily;
import com.nhnacademy.sensordata.entity.HumidityMaxMinWeekly;

import java.util.List;

public interface HumidityService {
    Humidity getHumidity();

    List<HumidityMaxMinDaily> getDailyHumidity();

    List<HumidityMaxMinWeekly> getWeeklyHumidity();
}
