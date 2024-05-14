package com.nhnacademy.sensordata.measurement.illumination;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IlluminationMean {
    private Instant time;
    private Float value;
}
