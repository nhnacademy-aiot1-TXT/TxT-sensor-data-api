package com.nhnacademy.sensordata.service;

import com.nhnacademy.sensordata.entity.Temperature;
import com.nhnacademy.sensordata.entity.TemperatureMaxMinDaily;
import com.nhnacademy.sensordata.entity.TemperatureMaxMinMonthly;
import com.nhnacademy.sensordata.entity.TemperatureMaxMinWeekly;

import java.util.List;

public interface TemperatureService {
    Temperature getTemperature();

    List<TemperatureMaxMinDaily> getDailyTemperatures();

    List<TemperatureMaxMinWeekly> getWeeklyTemperatures();

    List<TemperatureMaxMinMonthly> getMonthlyTemperatures();
}
