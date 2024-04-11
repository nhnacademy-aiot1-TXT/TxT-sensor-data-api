package com.nhnacademy.sensordata.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Measurement(name = "daily_extreme_humidity")
public class HumidityMaxMinWeekly {
    @Column(name = "time")
    private Instant time;
    @Column(name = "max_humidity")
    private double maxHumidity;
    @Column(name = "min_humidity")
    private double minHumidity;
}
