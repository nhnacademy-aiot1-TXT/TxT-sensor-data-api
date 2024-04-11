package com.nhnacademy.sensordata.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Measurement(name = "temperature_hourly")
public class TemperatureMaxMinDaily {
    @Column(name = "time")
    private Instant time;
    @Column(name = "max_temperature")
    private Double maxTemperature;
    @Column(name = "min_temperature")
    private Double minTemperature;
}
