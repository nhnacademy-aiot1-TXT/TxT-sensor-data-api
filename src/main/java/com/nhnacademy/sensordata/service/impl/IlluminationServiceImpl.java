package com.nhnacademy.sensordata.service.impl;

import com.nhnacademy.sensordata.entity.illumination.Illumination;
import com.nhnacademy.sensordata.entity.illumination.IlluminationMaxMinDaily;
import com.nhnacademy.sensordata.entity.illumination.IlluminationMaxMinMonthly;
import com.nhnacademy.sensordata.entity.illumination.IlluminationMaxMinWeekly;
import com.nhnacademy.sensordata.service.IlluminationService;
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
public class IlluminationServiceImpl implements IlluminationService {
    private final InfluxDBUtil influxDBUtil;
    private final InfluxDBResultMapper resultMapper;
    @Override
    public Illumination getIllumination() {
        String query = "select time, device, place, topic, value from illumination order by time desc limit 1";
        QueryResult queryResult = influxDBUtil.processingQuery(query);

        Illumination illumination =resultMapper.toPOJO(queryResult, Illumination.class).get(0);
        if (Objects.nonNull(illumination)) {
            illumination.setTime(illumination.getTime().plus(9, ChronoUnit.HOURS));
        }

        return illumination;
    }

    @Override
    public List<IlluminationMaxMinDaily> getDailyIlluminations() {
        LocalDate today = LocalDate.now();
        String query = String.format("SELECT time, max_illumination, min_illumination FROM illumination_hourly WHERE time >= '%sT15:00:00Z' AND time < '%sT15:00:00Z'", today.minusDays(1), today);
        QueryResult queryResult = influxDBUtil.processingQuery(query);

        List<IlluminationMaxMinDaily> illuminations = resultMapper.toPOJO(queryResult, IlluminationMaxMinDaily.class);

        illuminations = illuminations.stream()
                .map(illumination -> new IlluminationMaxMinDaily(
                                illumination.getTime().plus(9, ChronoUnit.HOURS),
                                illumination.getMaxIllumination(),
                                illumination.getMinIllumination()
                        )
                )
                .collect(Collectors.toList());

        return illuminations.isEmpty() ? Collections.emptyList() : illuminations;
    }

    @Override
    public List<IlluminationMaxMinWeekly> getWeeklyIlluminations() {
        LocalDate today = LocalDate.now();
        String dailyQuery = String.format("SELECT time, max_illumination, min_illumination FROM illumination_daily WHERE time >= '%sT15:00:00Z' AND time < '%sT15:00:00Z'", today.minusWeeks(1), today);
        String hourlyQuery = "SELECT time, max_illumination, min_illumination FROM illumination_hourly order by time desc limit 1";

        QueryResult dailyQueryResult = influxDBUtil.processingQuery(dailyQuery);
        QueryResult hourlyQueryResult = influxDBUtil.processingQuery(hourlyQuery);

        List<IlluminationMaxMinWeekly> illuminations = resultMapper.toPOJO(dailyQueryResult, IlluminationMaxMinWeekly.class);
        IlluminationMaxMinDaily illuminationMaxMinDaily = resultMapper.toPOJO(hourlyQueryResult, IlluminationMaxMinDaily.class).get(0);

        illuminations = illuminations.stream()
                .map(illumination -> new IlluminationMaxMinWeekly(
                                illumination.getTime().plus(9, ChronoUnit.HOURS),
                                illumination.getMaxIllumination(),
                                illumination.getMinIllumination()
                        )
                )
                .collect(Collectors.toList());

        if (Objects.nonNull(illuminationMaxMinDaily)) {
            illuminations.add(new IlluminationMaxMinWeekly(
                            illuminationMaxMinDaily.getTime().plus(9, ChronoUnit.HOURS),
                            illuminationMaxMinDaily.getMaxIllumination(),
                            illuminationMaxMinDaily.getMinIllumination()
                    )
            );
        }

        return illuminations.isEmpty() ? Collections.emptyList() : illuminations;
    }

    @Override
    public List<IlluminationMaxMinMonthly> getMonthlyIlluminations() {
        LocalDate today = LocalDate.now();
        String dailyQuery = String.format("SELECT time, max_illumination, min_illumination FROM illumination_daily WHERE time >= '%sT15:00:00Z' AND time < '%sT15:00:00Z'", today.minusMonths(1), today);
        String hourlyQuery = "SELECT time, max_illumination, min_illumination FROM illumination_hourly order by time desc limit 1";

        QueryResult dailyQueryResult = influxDBUtil.processingQuery(dailyQuery);
        QueryResult hourlyQueryResult = influxDBUtil.processingQuery(hourlyQuery);

        List<IlluminationMaxMinMonthly> illuminations = resultMapper.toPOJO(dailyQueryResult, IlluminationMaxMinMonthly.class);
        IlluminationMaxMinDaily illuminationMaxMinDaily = resultMapper.toPOJO(hourlyQueryResult, IlluminationMaxMinDaily.class).get(0);

        illuminations = illuminations.stream()
                .map(illumination -> new IlluminationMaxMinMonthly(
                                illumination.getTime().plus(9, ChronoUnit.HOURS),
                                illumination.getMaxIllumination(),
                                illumination.getMinIllumination()
                        )
                )
                .collect(Collectors.toList());

        if (Objects.nonNull(illuminationMaxMinDaily)) {
            illuminations.add(new IlluminationMaxMinMonthly(
                            illuminationMaxMinDaily.getTime().plus(9, ChronoUnit.HOURS),
                            illuminationMaxMinDaily.getMaxIllumination(),
                            illuminationMaxMinDaily.getMinIllumination()
                    )
            );
        }

        return illuminations.isEmpty() ? Collections.emptyList() : illuminations;
    }
}
