package com.nhnacademy.sensordata.entity.illumination;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Measurement(name = "illumination_daily")
public class IlluminationMaxMinWeekly {
    @Column(name = "time")
    private Instant time;
    @Column(name = "max_illumination")
    private Double maxIllumination;
    @Column(name = "min_illumination")
    private Double minIllumination;
}
