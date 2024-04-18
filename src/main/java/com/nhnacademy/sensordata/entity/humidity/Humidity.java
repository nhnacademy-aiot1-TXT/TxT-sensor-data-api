package com.nhnacademy.sensordata.entity.humidity;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * humidity 단일 조회 measurement class
 *
 * @author jongsikk
 * @version 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Measurement(name = "humidity")
public class Humidity {
    @Column(name = "time")
    private Instant time;
    @Column(name = "device")
    private String device;
    @Column(name = "place")
    private String place;
    @Column(name = "topic")
    private String topic;
    @Column(name = "value")
    private Float value;
}
