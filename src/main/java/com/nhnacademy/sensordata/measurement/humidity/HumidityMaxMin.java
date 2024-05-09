package com.nhnacademy.sensordata.measurement.humidity;

import com.influxdb.annotations.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * humidity 일별 조회 measurement class
 *
 * @author jongsikk
 * @version 1.0.0
 */
@Getter
@Setter
@AllArgsConstructor
public class HumidityMaxMin {
    @Column(name = "time")
    private Instant time;
    @Column(name = "max_humidity")
    private Float maxHumidity;
    @Column(name = "min_humidity")
    private Float minHumidity;
}
