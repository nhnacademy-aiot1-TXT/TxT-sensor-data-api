package com.nhnacademy.sensordata.service.impl;

import com.nhnacademy.sensordata.entity.Temperature;
import com.nhnacademy.sensordata.service.TemperatureService;
import lombok.RequiredArgsConstructor;
import org.influxdb.dto.BoundParameterQuery.QueryBuilder;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.springframework.data.influxdb.InfluxDBTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TemperatureServiceImpl implements TemperatureService {
    private final InfluxDBTemplate<Point> influxDBTemplate;

    @Override
    public Temperature getTemperature() {
        Query query = QueryBuilder.newQuery("select time, device, place, topic, value from temperature order by time desc limit 1")
                .forDatabase("tig")
                .create();

        QueryResult queryResult = influxDBTemplate.query(query);

        InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
        return resultMapper.toPOJO(queryResult, Temperature.class).get(0);
    }
}
