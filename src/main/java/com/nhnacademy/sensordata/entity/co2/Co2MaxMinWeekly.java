package com.nhnacademy.sensordata.entity.co2;

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
@Measurement(name = "co2_daily")
public class Co2MaxMinWeekly {
    @Column(name = "time")
    private Instant time;
    @Column(name = "max_co2")
    private Integer maxCo2;
    @Column(name = "min_co2")
    private Integer minCo2;
}