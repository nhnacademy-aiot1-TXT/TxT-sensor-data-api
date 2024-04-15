package com.nhnacademy.sensordata.entity.co2;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Measurement(name = "co2_hourly")
public class Co2MaxMinDaily {
    @Column(name = "time")
    private Instant time;
    @Column(name = "max_co2")
    private Integer maxCo2;
    @Column(name = "min_co2")
    private Integer minCo2;
}
