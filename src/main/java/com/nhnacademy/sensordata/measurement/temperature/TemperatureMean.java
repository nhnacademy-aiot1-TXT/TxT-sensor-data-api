package com.nhnacademy.sensordata.measurement.temperature;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemperatureMean {
    private Instant time;
    private Float value;
}