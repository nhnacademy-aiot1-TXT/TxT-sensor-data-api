package com.nhnacademy.sensordata.service.impl;

import com.nhnacademy.sensordata.entity.illumination.Illumination;
import com.nhnacademy.sensordata.entity.illumination.IlluminationMaxMinDaily;
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
}
