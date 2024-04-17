package com.nhnacademy.sensordata.service;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.nhnacademy.sensordata.entity.voc.Voc;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class VocServiceTest {
    @Autowired
    private VocService vocService;
    @MockBean
    private InfluxDBClient influxDBClient;
    @Mock
    private QueryApi queryApi;

    @Test
    void getVoc() {
        // given
        Instant time = Instant.now();
        String device = "test device";
        String place = "test place";
        String topic = "test topic";
        Integer value = 20;

        Voc voc = new Voc(time, device, place, topic, value);

        given(influxDBClient.getQueryApi()).willReturn(queryApi);
        given(queryApi.query(anyString(), eq(Voc.class))).willReturn(List.of(voc));

        Voc resultVoc = vocService.getVoc();

        assertAll(
                () -> assertEquals(voc.getTime(), resultVoc.getTime()),
                () -> assertEquals(voc.getDevice(), resultVoc.getDevice()),
                () -> assertEquals(voc.getPlace(), resultVoc.getPlace()),
                () -> assertEquals(voc.getTopic(), resultVoc.getTopic()),
                () -> assertEquals(voc.getValue(), resultVoc.getValue())
        );
    }
}