package com.nhnacademy.sensordata.entity;

import lombok.Getter;
import lombok.Setter;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import java.time.Instant;

@Getter
@Setter
@Measurement(name = "hourly_extreme_humidity")
public class HumidityMaxMin {
    @Column(name = "time")
    private Instant time;
    @Column(name = "max_humidity")
    private double maxHumidity;
    @Column(name = "min_humidity")
    private double minHumidity;
}
