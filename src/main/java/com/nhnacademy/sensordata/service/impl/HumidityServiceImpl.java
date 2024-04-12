package com.nhnacademy.sensordata.service.impl;

import com.nhnacademy.sensordata.entity.humidity.Humidity;
import com.nhnacademy.sensordata.entity.humidity.HumidityMaxMinDaily;
import com.nhnacademy.sensordata.entity.humidity.HumidityMaxMinMonthly;
import com.nhnacademy.sensordata.entity.humidity.HumidityMaxMinWeekly;
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
    private final InfluxDBResultMapper resultMapper;

    @Override
    public Humidity getHumidity() {
        Query query = QueryBuilder.newQuery("select time, device, place, topic, value from humidity order by time desc limit 1")
                .forDatabase("tig")
                .create();

        QueryResult queryResult = influxDBTemplate.query(query);

        Humidity humidity = resultMapper.toPOJO(queryResult, Humidity.class).get(0);

        if (Objects.nonNull(humidity)) {
            humidity.setTime(humidity.getTime().plus(9, ChronoUnit.HOURS));
        }

        return humidity;
    }

    @Override
    public List<HumidityMaxMinDaily> getDailyHumidity() {
        LocalDate today = LocalDate.now();
        Query query = QueryBuilder.newQuery(String.format("select * from hourly_extreme_humidity where time >= '%sT15:00:00Z' AND time < '%sT15:00:00Z'", today.minusDays(1), today))
                .forDatabase("tig")
                .create();

        QueryResult queryResult = influxDBTemplate.query(query);

        List<HumidityMaxMinDaily> humidityMaxMinList = resultMapper.toPOJO(queryResult, HumidityMaxMinDaily.class);

        humidityMaxMinList = humidityMaxMinList.stream()
                .map(humidity -> new HumidityMaxMinDaily(
                                humidity.getTime().plus(9, ChronoUnit.HOURS),
                                humidity.getMaxHumidity(),
                                humidity.getMinHumidity()
                        )
                )
                .collect(Collectors.toList());

        return humidityMaxMinList.isEmpty() ? Collections.emptyList() : humidityMaxMinList;
    }

    @Override
    public List<HumidityMaxMinWeekly> getWeeklyHumidity() {
        LocalDate today = LocalDate.now();
        Query query = QueryBuilder.newQuery(String.format("select * from daily_extreme_humidity where time >= '%sT15:00:00Z' AND time < '%sT15:00:00Z'", today.minusWeeks(1), today))
                .forDatabase("tig")
                .create();
        Query query2 = QueryBuilder.newQuery("select * from hourly_extreme_humidity order by time desc")
                .forDatabase("tig")
                .create();
        QueryResult queryResult = influxDBTemplate.query(query);
        QueryResult queryResult2 = influxDBTemplate.query(query2);

        List<HumidityMaxMinWeekly> humidityMaxMinList = resultMapper.toPOJO(queryResult, HumidityMaxMinWeekly.class);
        HumidityMaxMinDaily humidityLastHour = resultMapper.toPOJO(queryResult2, HumidityMaxMinDaily.class).get(0);

        humidityMaxMinList = humidityMaxMinList.stream()
                .map(humidity -> new HumidityMaxMinWeekly(
                        humidity.getTime().plus(9, ChronoUnit.HOURS),
                        humidity.getMaxHumidity(),
                        humidity.getMinHumidity()
                ))
                .collect(Collectors.toList());

        if (Objects.nonNull(humidityLastHour)) {
            humidityMaxMinList.add(new HumidityMaxMinWeekly(
                    humidityLastHour.getTime().plus(9, ChronoUnit.HOURS),
                    humidityLastHour.getMaxHumidity(),
                    humidityLastHour.getMinHumidity()));
        }

        return humidityMaxMinList.isEmpty() ? Collections.emptyList() : humidityMaxMinList;
    }

    @Override
    public List<HumidityMaxMinMonthly> getMonthlyHumidity() {
        LocalDate today = LocalDate.now();
        Query query = QueryBuilder.newQuery(String.format("select * from daily_extreme_humidity where time >= '%sT15:00:00Z' AND time < '%sT15:00:00Z'", today.minusMonths(1), today))
                .forDatabase("tig")
                .create();
        Query query2 = QueryBuilder.newQuery("select * from hourly_extreme_humidity order by time desc")
                .forDatabase("tig")
                .create();
        QueryResult queryResult = influxDBTemplate.query(query);
        QueryResult queryResult2 = influxDBTemplate.query(query2);

        List<HumidityMaxMinMonthly> humidityMaxMinList = resultMapper.toPOJO(queryResult, HumidityMaxMinMonthly.class);
        HumidityMaxMinDaily humidityLastHour = resultMapper.toPOJO(queryResult2, HumidityMaxMinDaily.class).get(0);

        humidityMaxMinList = humidityMaxMinList.stream()
                .map(humidity -> new HumidityMaxMinMonthly(
                                humidity.getTime().plus(9, ChronoUnit.HOURS),
                                humidity.getMaxHumidity(),
                                humidity.getMinHumidity()
                        )
                )
                .collect(Collectors.toList());

        if (Objects.nonNull(humidityLastHour)) {
            humidityMaxMinList.add(new HumidityMaxMinMonthly(
                    humidityLastHour.getTime().plus(9, ChronoUnit.HOURS),
                    humidityLastHour.getMaxHumidity(),
                    humidityLastHour.getMinHumidity()));
        }

        return humidityMaxMinList.isEmpty() ? Collections.emptyList() : humidityMaxMinList;
    }
}
