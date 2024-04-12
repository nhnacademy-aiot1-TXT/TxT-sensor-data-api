package com.nhnacademy.sensordata.service.impl;

import com.nhnacademy.sensordata.entity.Illumination;
import com.nhnacademy.sensordata.service.IlluminationService;
import com.nhnacademy.sensordata.utils.InfluxDBUtil;
import lombok.RequiredArgsConstructor;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class IlluminationServiceImpl implements IlluminationService {
    private final InfluxDBUtil influxDBUtil;
    private final InfluxDBResultMapper resultMapper;
    @Override
    public Illumination getIllumination() {
        String query = "select time, device, place, topic, value from illumination order by time desc limit 1";
        QueryResult queryResult = influxDBUtil.processingQuery(query);

        Illumination illumination =resultMapper.toPOJO(queryResult, Illumination.class).get(0);
        if (Objects.nonNull(illumination)) {
            illumination.setTime(illumination.getTime().plus(9, ChronoUnit.HOURS));
        }

        return illumination;
    }
}
