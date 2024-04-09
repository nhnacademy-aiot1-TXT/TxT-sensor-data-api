package com.nhnacademy.sensordata.entity;

import lombok.Data;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import java.time.Instant;


@Data
@Measurement(name = "temperature")
public class Temperature {
    @Column(name = "time")
    private Instant time;
    @Column(name = "device")
    private String device;
    @Column(name = "place")
    private String place;
    @Column(name = "topic")
    private String topic;
    @Column(name = "value")
    private Double value;
}
