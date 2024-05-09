package com.nhnacademy.sensordata.measurement.co2;

import com.influxdb.annotations.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * co2 일별 조회 measurement class
 *
 * @author jongsikk
 * @version 1.0.0
 */
@Getter
@Setter
@AllArgsConstructor
public class Co2MaxMin {
    @Column(name = "time")
    private Instant time;
    @Column(name = "max_co2")
    private Integer maxCo2;
    @Column(name = "min_co2")
    private Integer minCo2;
}
