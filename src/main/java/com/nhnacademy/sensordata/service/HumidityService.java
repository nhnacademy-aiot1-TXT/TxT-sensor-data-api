package com.nhnacademy.sensordata.service;

import com.nhnacademy.sensordata.entity.Humidity;
import com.nhnacademy.sensordata.entity.HumidityMaxMin;

import java.util.List;

public interface HumidityService {
    Humidity getHumidity();

    List<HumidityMaxMin> getDailyHumidity();
}
