package com.nhnacademy.sensordata.service.impl;

import com.nhnacademy.sensordata.entity.voc.Voc;
import com.nhnacademy.sensordata.service.VocService;
import com.nhnacademy.sensordata.utils.InfluxDBUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VocServiceImpl implements VocService {
    private final InfluxDBUtil influxDBUtil;

    @Override
    public Voc getVoc() {
        String query = "select time, device,place, topic, value from voc order by time desc limit 1";
//        QueryResult queryResult = influxDBUtil.processingQuery(query);
//
//        Voc voc = resultMapper.toPOJO(queryResult, Voc.class).get(0);
//        if (Objects.nonNull(voc)) {
//            voc.setTime(voc.getTime().plus(9, ChronoUnit.HOURS));
//        }
//
//        return voc;
        return null;
    }
}
