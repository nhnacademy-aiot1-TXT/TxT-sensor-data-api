package com.nhnacademy.sensordata.measurement.humidity;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * humidity 월별 조회 measurement class
 *
 * @author jongsikk
 * @version 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Measurement(name = "humidity_daily")
public class HumidityMaxMinMonthly {
    @Column(name = "time")
    private Instant time;
    @Column(name = "max_humidity")
    private Float maxHumidity;
    @Column(name = "min_humidity")
    private Float minHumidity;
}
