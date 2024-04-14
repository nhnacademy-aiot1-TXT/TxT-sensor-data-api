package com.nhnacademy.sensordata.service.impl;

import com.nhnacademy.sensordata.entity.people_count.PeopleCount;
import com.nhnacademy.sensordata.service.PeopleCountService;
import com.nhnacademy.sensordata.utils.InfluxDBUtil;
import lombok.RequiredArgsConstructor;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PeopleCountServiceImpl implements PeopleCountService {
    private final InfluxDBUtil influxDBUtil;
    private final InfluxDBResultMapper resultMapper;

    @Override
    public PeopleCount getPeopleCount() {
        QueryResult queryResult = influxDBUtil.processingQuery("select * from people order by time desc limit 1");

        PeopleCount peopleCount = resultMapper.toPOJO(queryResult, PeopleCount.class).get(0);

        if (Objects.nonNull(peopleCount)) {
            peopleCount.setTime(peopleCount.getTime().plus(9, ChronoUnit.HOURS));
        }

        return peopleCount;
    }
}
