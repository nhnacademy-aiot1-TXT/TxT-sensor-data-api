package com.nhnacademy.sensordata.measurement.people_count;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * people-count 단일 조회 measurement class
 *
 * @author jongsikk
 * @version 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Measurement(name = "people_total_count")
public class PeopleCount {
    @Column(name = "time")
    private Instant time;
    @Column(name = "value")
    private Integer count;
}
