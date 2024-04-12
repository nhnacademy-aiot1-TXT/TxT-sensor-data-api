package com.nhnacademy.sensordata.config;

import org.influxdb.dto.Point;
import org.influxdb.impl.InfluxDBResultMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.influxdb.DefaultInfluxDBTemplate;
import org.springframework.data.influxdb.InfluxDBConnectionFactory;
import org.springframework.data.influxdb.InfluxDBProperties;
import org.springframework.data.influxdb.InfluxDBTemplate;
import org.springframework.data.influxdb.converter.PointConverter;

@Configuration
@EnableConfigurationProperties(InfluxDBProperties.class)
public class InfluxDBConfig {
    @Bean
    public InfluxDBConnectionFactory connectionFactory(@Qualifier("spring.influxdb-org.springframework.data.influxdb.InfluxDBProperties") final InfluxDBProperties properties) {
        return new InfluxDBConnectionFactory(properties);
    }

    @Bean
    public InfluxDBTemplate<Point> influxDBTemplate(final InfluxDBConnectionFactory connectionFactory) {
        return new InfluxDBTemplate<>(connectionFactory, new PointConverter());
    }

    @Bean
    public DefaultInfluxDBTemplate defaultTemplate(final InfluxDBConnectionFactory connectionFactory) {
        return new DefaultInfluxDBTemplate(connectionFactory);
    }

    @Bean
    public InfluxDBResultMapper influxDBResultMapper() {
        return new InfluxDBResultMapper();
    }
}