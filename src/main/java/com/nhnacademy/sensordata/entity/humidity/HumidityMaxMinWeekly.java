package com.nhnacademy.sensordata.entity.humidity;

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
@Measurement(name = "daily_extreme_humidity")
public class HumidityMaxMinWeekly {
    @Column(name = "time")
    private Instant time;
    @Column(name = "max_humidity")
    private double maxHumidity;
    @Column(name = "min_humidity")
    private double minHumidity;
}
