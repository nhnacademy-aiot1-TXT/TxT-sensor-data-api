package com.nhnacademy.sensordata.service;

import com.nhnacademy.sensordata.exception.VocNotFoundException;
import com.nhnacademy.sensordata.measurement.voc.Voc;
import com.nhnacademy.sensordata.util.InfluxDBUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class VocServiceTest {
    @Autowired
    private VocService vocService;
    @MockBean
    private InfluxDBUtil influxDBUtil;

    @Test
    void getVoc() {
        // given
        Instant time = Instant.now();
        String device = "test device";
        String place = "test place";
        String topic = "test topic";
        Integer value = 20;

        Voc voc = new Voc(time, device, place, topic, value);

        given(influxDBUtil.getSensorData(anyString(), eq(Voc.class))).willReturn(Optional.of(voc));

        Voc resultVoc = vocService.getVoc();

        assertAll(
                () -> assertEquals(voc.getTime(), resultVoc.getTime()),
                () -> assertEquals(voc.getDevice(), resultVoc.getDevice()),
                () -> assertEquals(voc.getPlace(), resultVoc.getPlace()),
                () -> assertEquals(voc.getTopic(), resultVoc.getTopic()),
                () -> assertEquals(voc.getValue(), resultVoc.getValue())
        );
    }

    @Test
    void getVocException() {
        given(influxDBUtil.getSensorData(anyString(), eq(Voc.class))).willReturn(Optional.empty());

        assertThrows(VocNotFoundException.class, () -> vocService.getVoc());
    }
}