package com.nhnacademy.sensordata.service;

import com.nhnacademy.sensordata.entity.temperature.Temperature;
import com.nhnacademy.sensordata.entity.temperature.TemperatureMaxMinDaily;
import com.nhnacademy.sensordata.entity.temperature.TemperatureMaxMinMonthly;
import com.nhnacademy.sensordata.entity.temperature.TemperatureMaxMinWeekly;

import java.util.List;

public interface TemperatureService {
    Temperature getTemperature();

    List<TemperatureMaxMinDaily> getDailyTemperatures();

    List<TemperatureMaxMinWeekly> getWeeklyTemperatures();

    List<TemperatureMaxMinMonthly> getMonthlyTemperatures();
}
