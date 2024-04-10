package com.nhnacademy.sensordata.service.impl;

import com.nhnacademy.sensordata.entity.Temperature;
import com.nhnacademy.sensordata.entity.TemperatureMaxMin;
import com.nhnacademy.sensordata.service.TemperatureService;
import lombok.RequiredArgsConstructor;
import org.influxdb.dto.BoundParameterQuery.QueryBuilder;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.springframework.data.influxdb.InfluxDBTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

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

    @Override
    public List<TemperatureMaxMin> getDailyTemperatures() {
        LocalDate today = LocalDate.now();

        Query query = QueryBuilder.newQuery(String.format("SELECT time, max_temperature, min_temperature FROM temperature_hourly WHERE time >= '%sT00:00:00Z' AND time <= '%sT00:00:00Z'", today, today.plusDays(1)))
                .forDatabase("tig")
                .create();

        QueryResult queryResult = influxDBTemplate.query(query);

        InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
        List<TemperatureMaxMin> temperatures = resultMapper.toPOJO(queryResult, TemperatureMaxMin.class);

        return temperatures.isEmpty() ? Collections.emptyList() : temperatures;
    }
}
