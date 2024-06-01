package com.nhnacademy.sensordata.measurement.illumination;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * illumination 평균 조회 measurement class
 *
 * @author parksangwon
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IlluminationMean {
    private Instant time;
    private Float value;
}
