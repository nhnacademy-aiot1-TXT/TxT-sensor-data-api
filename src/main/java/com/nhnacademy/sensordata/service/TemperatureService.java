package com.nhnacademy.sensordata.service;

import com.nhnacademy.sensordata.entity.Temperature;
import com.nhnacademy.sensordata.entity.TemperatureMaxMin;

import java.util.List;

public interface TemperatureService {
    Temperature getTemperature();

    List<TemperatureMaxMin> getDailyTemperatures();
}
