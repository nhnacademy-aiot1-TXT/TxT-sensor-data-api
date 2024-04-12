package com.nhnacademy.sensordata.service;

import com.nhnacademy.sensordata.entity.illumination.Illumination;
import com.nhnacademy.sensordata.entity.illumination.IlluminationMaxMinDaily;
import com.nhnacademy.sensordata.entity.illumination.IlluminationMaxMinWeekly;

import java.util.List;

public interface IlluminationService {
    Illumination getIllumination();

    List<IlluminationMaxMinDaily> getDailyIlluminations();

    List<IlluminationMaxMinWeekly> getWeeklyIlluminations();
}
