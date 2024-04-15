package com.nhnacademy.sensordata.utils;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.query.FluxTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InfluxDBUtil {
    private final InfluxDBClient influxDBClient;

    public List<FluxTable> processingQuery(String queryString) {
        return influxDBClient.getQueryApi().query(queryString);
    }
}
