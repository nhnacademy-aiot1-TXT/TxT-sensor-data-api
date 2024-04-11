package com.nhnacademy.sensordata.service.impl;

import com.nhnacademy.sensordata.entity.Temperature;
import com.nhnacademy.sensordata.entity.TemperatureMaxMinDaily;
import com.nhnacademy.sensordata.entity.TemperatureMaxMinMonthly;
import com.nhnacademy.sensordata.entity.TemperatureMaxMinWeekly;
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
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

        Temperature temperature = resultMapper.toPOJO(queryResult, Temperature.class).get(0);
        if (Objects.nonNull(temperature)) {
            temperature.setTime(temperature.getTime().plus(9, ChronoUnit.HOURS));
        }

        return temperature;
    }

    @Override
    public List<TemperatureMaxMinDaily> getDailyTemperatures() {
        LocalDate today = LocalDate.now();

        Query query = QueryBuilder.newQuery(String.format("SELECT time, max_temperature, min_temperature FROM temperature_hourly WHERE time >= '%sT15:00:00Z' AND time < '%sT15:00:00Z'", today.minusDays(1), today))
                .forDatabase("tig")
                .create();

        QueryResult queryResult = influxDBTemplate.query(query);

        InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
        List<TemperatureMaxMinDaily> temperatures = resultMapper.toPOJO(queryResult, TemperatureMaxMinDaily.class);

        temperatures = temperatures.stream()
                .peek(humidity -> {
                    if (Objects.nonNull(humidity)) {
                        humidity.setTime(humidity.getTime().plus(9, ChronoUnit.HOURS));
                    }
                })
                .collect(Collectors.toList());

        return temperatures.isEmpty() ? Collections.emptyList() : temperatures;
    }

    @Override
    public List<TemperatureMaxMinWeekly> getWeeklyTemperatures() {
        LocalDate today = LocalDate.now();

        Query query = QueryBuilder.newQuery(String.format("SELECT time, max_temperature, min_temperature FROM temperature_daily WHERE time >= '%sT15:00:00Z' AND time < '%sT15:00:00Z'", today.minusWeeks(1), today))
                .forDatabase("tig")
                .create();

        Query query2 = QueryBuilder.newQuery("SELECT time, max_temperature, min_temperature FROM temperature_hourly order by time desc limit 1")
                .forDatabase("tig")
                .create();

        QueryResult queryResult = influxDBTemplate.query(query);
        QueryResult queryResult2 = influxDBTemplate.query(query2);

        InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
        List<TemperatureMaxMinWeekly> temperatures = resultMapper.toPOJO(queryResult, TemperatureMaxMinWeekly.class);
        TemperatureMaxMinDaily temperatureMaxMinDaily = resultMapper.toPOJO(queryResult2, TemperatureMaxMinDaily.class).get(0);

        temperatures = temperatures.stream()
                .peek(humidity -> {
                    if (Objects.nonNull(humidity)) {
                        humidity.setTime(humidity.getTime().plus(9, ChronoUnit.HOURS));
                    }
                })
                .collect(Collectors.toList());

        if (Objects.nonNull(temperatureMaxMinDaily)) {
            temperatures.add(new TemperatureMaxMinWeekly(temperatureMaxMinDaily.getTime(), temperatureMaxMinDaily.getMaxTemperature(), temperatureMaxMinDaily.getMinTemperature()));
        }

        return temperatures.isEmpty() ? Collections.emptyList() : temperatures;
    }

    @Override
    public List<TemperatureMaxMinMonthly> getMonthlyTemperatures() {
        LocalDate today = LocalDate.now();

        Query query = QueryBuilder.newQuery(String.format("SELECT time, max_temperature, min_temperature FROM temperature_daily WHERE time >= '%sT15:00:00Z' AND time < '%sT15:00:00Z'", today.minusMonths(1), today))
                .forDatabase("tig")
                .create();

        Query query2 = QueryBuilder.newQuery("SELECT time, max_temperature, min_temperature FROM temperature_hourly order by time desc limit 1")
                .forDatabase("tig")
                .create();

        QueryResult queryResult = influxDBTemplate.query(query);
        QueryResult queryResult2 = influxDBTemplate.query(query2);

        InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
        List<TemperatureMaxMinMonthly> temperatures = resultMapper.toPOJO(queryResult, TemperatureMaxMinMonthly.class);
        TemperatureMaxMinDaily temperatureMaxMinDaily = resultMapper.toPOJO(queryResult2, TemperatureMaxMinDaily.class).get(0);

        temperatures = temperatures.stream()
                .peek(humidity -> {
                    if (Objects.nonNull(humidity)) {
                        humidity.setTime(humidity.getTime().plus(9, ChronoUnit.HOURS));
                    }
                })
                .collect(Collectors.toList());

        if (Objects.nonNull(temperatureMaxMinDaily)) {
            temperatures.add(new TemperatureMaxMinMonthly(temperatureMaxMinDaily.getTime(), temperatureMaxMinDaily.getMaxTemperature(), temperatureMaxMinDaily.getMinTemperature()));
        }

        return temperatures.isEmpty() ? Collections.emptyList() : temperatures;
    }
}
