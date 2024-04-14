package com.nhnacademy.sensordata.entity.people_count;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Measurement(name = "people")
public class PeopleCount {
    @Column(name = "time")
    private Instant time;
    @Column(name = "total_in_count")
    private Integer inCount;
    @Column(name = "total_out_count")
    private Integer outCount;
}
