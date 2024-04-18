package com.nhnacademy.sensordata.measurement.temperature;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * 온도 월별 조회 measurement class
 *
 * @author parksangwon
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Measurement(name = "temperature_daily")
public class TemperatureMaxMinMonthly {
    @Column(name = "time")
    private Instant time;
    @Column(name = "max_temperature")
    private Float maxTemperature;
    @Column(name = "min_temperature")
    private Float minTemperature;
}
