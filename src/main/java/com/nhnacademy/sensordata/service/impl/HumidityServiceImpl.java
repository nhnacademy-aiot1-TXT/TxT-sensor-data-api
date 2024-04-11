package com.nhnacademy.sensordata.service.impl;

import com.nhnacademy.sensordata.entity.Humidity;
import com.nhnacademy.sensordata.entity.HumidityMaxMin;
import com.nhnacademy.sensordata.service.HumidityService;
import lombok.RequiredArgsConstructor;
import org.influxdb.dto.BoundParameterQuery.QueryBuilder;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.springframework.data.influxdb.InfluxDBTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

        Humidity humidity = resultMapper.toPOJO(queryResult, Humidity.class).get(0);

        if (Objects.nonNull(humidity)) {
            humidity.setTime(humidity.getTime().plus(9, ChronoUnit.HOURS));
        }

        return humidity;
    }

    @Override
    public List<HumidityMaxMin> getDailyHumidity() {
        LocalDate today = LocalDate.now();
        Query query = QueryBuilder.newQuery(String.format("select * from hourly_extreme_humidity where time >= '%sT15:00:00Z' AND time <= '%sT15:00:00Z'", today.minusDays(1), today))
                .forDatabase("tig")
                .create();

        QueryResult queryResult = influxDBTemplate.query(query);

        InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();

        List<HumidityMaxMin> humidityMaxMinList = resultMapper.toPOJO(queryResult, HumidityMaxMin.class);
        humidityMaxMinList = humidityMaxMinList.stream()
                .peek(humidity -> {
                    if (Objects.nonNull(humidity)) {
                        humidity.setTime(humidity.getTime().plus(9, ChronoUnit.HOURS));
                    }
                })
                .collect(Collectors.toList());

        return humidityMaxMinList.isEmpty() ? Collections.emptyList() : humidityMaxMinList;
    }
}
