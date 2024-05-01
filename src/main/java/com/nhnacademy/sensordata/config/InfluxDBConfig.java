package com.nhnacademy.sensordata.config;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.nhnacademy.sensordata.properties.InfluxDBProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * InfluxDB 관련 설정 class
 *
 * @author parksangwon
 * @version 1.0.0
 */
@Configuration
@RequiredArgsConstructor
public class InfluxDBConfig {
    private final InfluxDBProperties influxDBProperties;

    /**
     * influxdb url, token, organization, bucket을 설정해서 반환하는 Bean
     *
     * @return 설정이 완료된 influxdb client
     */
    @Bean
    public InfluxDBClient influxDB() {
        return InfluxDBClientFactory.create(
                influxDBProperties.getUrl(),
                influxDBProperties.getToken().toCharArray(),
                influxDBProperties.getOrg(),
                influxDBProperties.getBucket()
        );
    }
}