package com.nhnacademy.sensordata.service.impl;

import com.nhnacademy.sensordata.entity.Humidity;
import com.nhnacademy.sensordata.service.HumidityService;
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
public class HumidityServiceImpl implements HumidityService {
    private final InfluxDBTemplate<Point> influxDBTemplate;

    @Override
    public Humidity getHumidity() {
        Query query = QueryBuilder.newQuery("select time, device, place, topic, value from humidity order by time desc limit 1")
                .forDatabase("tig")
                .create();

        QueryResult queryResult = influxDBTemplate.query(query);

        InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
        return resultMapper.toPOJO(queryResult, Humidity.class).get(0);
    }
}
