package com.nhnacademy.sensordata.utils;

import lombok.RequiredArgsConstructor;
import org.influxdb.dto.BoundParameterQuery;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.data.influxdb.InfluxDBTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InfluxDBUtil {
    private final InfluxDBTemplate<Point> influxDBTemplate;

    public QueryResult processingQuery(String queryString) {
        Query query = BoundParameterQuery.QueryBuilder.newQuery(queryString)
                .forDatabase("tig")
                .create();

        return influxDBTemplate.query(query);
    }
}
