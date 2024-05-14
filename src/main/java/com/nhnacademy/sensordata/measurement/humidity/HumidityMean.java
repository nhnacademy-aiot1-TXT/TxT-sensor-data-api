package com.nhnacademy.sensordata.measurement.humidity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HumidityMean {
    Instant time;
    Float value;
}
