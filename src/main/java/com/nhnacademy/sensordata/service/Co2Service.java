package com.nhnacademy.sensordata.service;

import com.nhnacademy.sensordata.entity.co2.Co2;
import com.nhnacademy.sensordata.entity.co2.Co2MaxMinDaily;
import com.nhnacademy.sensordata.entity.co2.Co2MaxMinMonthly;
import com.nhnacademy.sensordata.entity.co2.Co2MaxMinWeekly;

import java.util.List;

public interface Co2Service {
    Co2 getCo2();

    List<Co2MaxMinDaily> getDailyCo2();

    List<Co2MaxMinWeekly> getWeeklyCo2();

    List<Co2MaxMinMonthly> getMonthlyCo2();
}
