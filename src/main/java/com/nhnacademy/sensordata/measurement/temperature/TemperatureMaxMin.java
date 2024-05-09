package com.nhnacademy.sensordata.measurement.temperature;

import com.influxdb.annotations.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * 온도 일별 조회 measurement class
 *
 * @author parksangwon
 * @version 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TemperatureMaxMin {
    @Column(name = "time")
    private Instant time;
    @Column(name = "max_temperature")
    private Float maxTemperature;
    @Column(name = "min_temperature")
    private Float minTemperature;
}
