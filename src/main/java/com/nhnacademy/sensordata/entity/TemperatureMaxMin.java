package com.nhnacademy.sensordata.entity;

import lombok.Data;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import java.time.Instant;

@Data
@Measurement(name = "temperature_hourly")
public class TemperatureMaxMin {
    @Column(name = "time")
    private Instant time;
    @Column(name = "max_temperature")
    private double maxTemperature;
    @Column(name = "min_temperature")
    private double minTemperature;
}
