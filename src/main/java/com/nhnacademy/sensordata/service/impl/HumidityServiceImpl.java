package com.nhnacademy.sensordata.service.impl;

import com.nhnacademy.sensordata.entity.Humidity;
import com.nhnacademy.sensordata.entity.HumidityMaxMinDaily;
import com.nhnacademy.sensordata.entity.HumidityMaxMinMonthly;
import com.nhnacademy.sensordata.entity.HumidityMaxMinWeekly;
import com.nhnacademy.sensordata.service.HumidityService;
import com.nhnacademy.sensordata.utils.InfluxDBUtil;
import lombok.RequiredArgsConstructor;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
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
    private final InfluxDBResultMapper resultMapper;
    private final InfluxDBUtil influxDBUtil;

    @Override
    public Humidity getHumidity() {
        QueryResult queryResult = influxDBUtil.processingQuery("select time, device, place, topic, value from humidity order by time desc limit 1");

        Humidity humidity = resultMapper.toPOJO(queryResult, Humidity.class).get(0);

        if (Objects.nonNull(humidity)) {
            humidity.setTime(humidity.getTime().plus(9, ChronoUnit.HOURS));
        }

        return humidity;
    }

    @Override
    public List<HumidityMaxMinDaily> getDailyHumidity() {
        QueryResult queryResult = influxDBUtil.processingQuery(String.format("select * from hourly_extreme_humidity where time >= '%sT15:00:00Z' AND time < '%sT15:00:00Z'", today.minusDays(1), today));

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

        QueryResult queryResult = influxDBUtil.processingQuery(String.format("select * from daily_extreme_humidity where time >= '%sT15:00:00Z' AND time < '%sT15:00:00Z'", today.minusWeeks(1), today));
        QueryResult queryResult2 = influxDBUtil.processingQuery("select * from hourly_extreme_humidity order by time desc");

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

        QueryResult queryResult = influxDBUtil.processingQuery(String.format("select * from daily_extreme_humidity where time >= '%sT15:00:00Z' AND time < '%sT15:00:00Z'", today.minusMonths(1), today));
        QueryResult queryResult2 = influxDBUtil.processingQuery("select * from hourly_extreme_humidity order by time desc");

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
