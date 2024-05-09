package com.nhnacademy.sensordata.measurement.illumination;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;


/**
 * 조도 단일 조회 measurement class
 *
 * @author parksangwon
 * @version 1.0.0
 */
@Getter
@Setter
@AllArgsConstructor
@Measurement(name = "illumination")
public class Illumination {
    @Column(name = "time")
    private Instant time;
    @Column(name = "device")
    private String device;
    @Column(name = "place")
    private String place;
    @Column(name = "topic")
    private String topic;
    @Column(name = "value")
    private Integer value;
}
