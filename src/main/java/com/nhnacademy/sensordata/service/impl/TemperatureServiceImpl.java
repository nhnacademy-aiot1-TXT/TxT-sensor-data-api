package com.nhnacademy.sensordata.service.impl;

import com.nhnacademy.sensordata.entity.temperature.Temperature;
import com.nhnacademy.sensordata.entity.temperature.TemperatureMaxMinDaily;
import com.nhnacademy.sensordata.entity.temperature.TemperatureMaxMinMonthly;
import com.nhnacademy.sensordata.entity.temperature.TemperatureMaxMinWeekly;
import com.nhnacademy.sensordata.service.TemperatureService;
import com.nhnacademy.sensordata.utils.InfluxDBUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TemperatureServiceImpl implements TemperatureService {
    private final InfluxDBUtil influxDBUtil;

    @Override
    public Temperature getTemperature() {
        String query = "select time, device, place, topic, value from temperature order by time desc limit 1";
//        QueryResult queryResult = influxDBUtil.processingQuery(query);
//
//        Temperature temperature = resultMapper.toPOJO(queryResult, Temperature.class).get(0);
//        if (Objects.nonNull(temperature)) {
//            temperature.setTime(temperature.getTime().plus(9, ChronoUnit.HOURS));
//        }
//
//        return temperature;
        return null;
    }

    @Override
    public List<TemperatureMaxMinDaily> getDailyTemperatures() {
        LocalDate today = LocalDate.now();
        String query = String.format("SELECT time, max_temperature, min_temperature FROM temperature_hourly WHERE time >= '%sT15:00:00Z' AND time < '%sT15:00:00Z'", today.minusDays(1), today);
//        QueryResult queryResult = influxDBUtil.processingQuery(query);
//
//        List<TemperatureMaxMinDaily> temperatures = resultMapper.toPOJO(queryResult, TemperatureMaxMinDaily.class);
//
//        temperatures = temperatures.stream()
//                .map(temperature -> new TemperatureMaxMinDaily(
//                                temperature.getTime().plus(9, ChronoUnit.HOURS),
//                                temperature.getMaxTemperature(),
//                                temperature.getMinTemperature()
//                        )
//                )
//                .collect(Collectors.toList());
//
//        return temperatures.isEmpty() ? Collections.emptyList() : temperatures;
        return null;
    }

    @Override
    public List<TemperatureMaxMinWeekly> getWeeklyTemperatures() {
        LocalDate today = LocalDate.now();
        String dailyQuery = String.format("SELECT time, max_temperature, min_temperature FROM temperature_daily WHERE time >= '%sT15:00:00Z' AND time < '%sT15:00:00Z'", today.minusWeeks(1), today);
        String hourlyQuery = "SELECT time, max_temperature, min_temperature FROM temperature_hourly order by time desc limit 1";

//        QueryResult dailyQueryResult = influxDBUtil.processingQuery(dailyQuery);
//        QueryResult hourlyQueryResult = influxDBUtil.processingQuery(hourlyQuery);
//
//        List<TemperatureMaxMinWeekly> temperatures = resultMapper.toPOJO(dailyQueryResult, TemperatureMaxMinWeekly.class);
//        TemperatureMaxMinDaily temperatureMaxMinDaily = resultMapper.toPOJO(hourlyQueryResult, TemperatureMaxMinDaily.class).get(0);
//
//        temperatures = temperatures.stream()
//                .map(temperature -> new TemperatureMaxMinWeekly(
//                                temperature.getTime().plus(9, ChronoUnit.HOURS),
//                                temperature.getMaxTemperature(),
//                                temperature.getMinTemperature()
//                        )
//                )
//                .collect(Collectors.toList());
//
//        if (Objects.nonNull(temperatureMaxMinDaily)) {
//            temperatures.add(new TemperatureMaxMinWeekly(
//                            temperatureMaxMinDaily.getTime().plus(9, ChronoUnit.HOURS),
//                            temperatureMaxMinDaily.getMaxTemperature(),
//                            temperatureMaxMinDaily.getMinTemperature()
//                    )
//            );
//        }
//
//        return temperatures.isEmpty() ? Collections.emptyList() : temperatures;
        return null;
    }

    @Override
    public List<TemperatureMaxMinMonthly> getMonthlyTemperatures() {
        LocalDate today = LocalDate.now();
        String dailyQuery = String.format("SELECT time, max_temperature, min_temperature FROM temperature_daily WHERE time >= '%sT15:00:00Z' AND time < '%sT15:00:00Z'", today.minusMonths(1), today);
        String hourlyQuery = "SELECT time, max_temperature, min_temperature FROM temperature_hourly order by time desc limit 1";

//        QueryResult dailyQueryResult = influxDBUtil.processingQuery(dailyQuery);
//        QueryResult hourlyQueryResult = influxDBUtil.processingQuery(hourlyQuery);
//
//        List<TemperatureMaxMinMonthly> temperatures = resultMapper.toPOJO(dailyQueryResult, TemperatureMaxMinMonthly.class);
//        TemperatureMaxMinDaily temperatureMaxMinDaily = resultMapper.toPOJO(hourlyQueryResult, TemperatureMaxMinDaily.class).get(0);
//
//        temperatures = temperatures.stream()
//                .map(temperature -> new TemperatureMaxMinMonthly(
//                                temperature.getTime().plus(9, ChronoUnit.HOURS),
//                                temperature.getMaxTemperature(),
//                                temperature.getMinTemperature()
//                        )
//                )
//                .collect(Collectors.toList());
//
//        if (Objects.nonNull(temperatureMaxMinDaily)) {
//            temperatures.add(new TemperatureMaxMinMonthly(
//                            temperatureMaxMinDaily.getTime().plus(9, ChronoUnit.HOURS),
//                            temperatureMaxMinDaily.getMaxTemperature(),
//                            temperatureMaxMinDaily.getMinTemperature()
//                    )
//            );
//        }
//
//        return temperatures.isEmpty() ? Collections.emptyList() : temperatures;
        return null;
    }
}
