package com.nhnacademy.sensordata.service.impl;

import com.nhnacademy.sensordata.entity.Illumination;
import com.nhnacademy.sensordata.service.IlluminationService;
import lombok.RequiredArgsConstructor;
import org.influxdb.dto.Point;
import org.influxdb.impl.InfluxDBResultMapper;
import org.springframework.data.influxdb.InfluxDBTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IlluminationServiceImpl implements IlluminationService {
    private final InfluxDBTemplate<Point> influxDBTemplate;
    private final InfluxDBResultMapper influxDBResultMapper;
    @Override
    public Illumination getIllumination() {
        return null;
    }
}
