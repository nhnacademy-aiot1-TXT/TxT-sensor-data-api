package com.nhnacademy.sensordata.measurement.co2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * co2 평균 조회 measurement class
 *
 * @author parksangwon
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Co2Mean {
    Instant time;
    Float value;
}
