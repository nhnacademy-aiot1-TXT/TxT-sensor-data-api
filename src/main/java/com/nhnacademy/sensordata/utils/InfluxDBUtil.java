package com.nhnacademy.sensordata.utils;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.domain.InfluxQLQuery;
import com.influxdb.query.InfluxQLQueryResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InfluxDBUtil {
    private final InfluxDBClient influxDBClient;

    public <M> List<M> processingQuery(String queryString, Class<M> clazz) {
        InfluxQLQueryResult queryResult = influxDBClient.getInfluxQLQueryApi().query(new InfluxQLQuery("select time, device, place, topic, value from temperature order by time desc limit 1", "TxT-iot"));
        

        return influxDBClient.getQueryApi().query(queryString, clazz);
    }

}
