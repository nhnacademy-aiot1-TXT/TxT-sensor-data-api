//package com.nhnacademy.sensordata.service;
//
//import com.nhnacademy.sensordata.entity.voc.Voc;
//import com.nhnacademy.sensordata.utils.InfluxDBUtil;
//import org.influxdb.dto.QueryResult;
//import org.influxdb.impl.InfluxDBResultMapper;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//
//import java.time.Instant;
//import java.time.temporal.ChronoUnit;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.BDDMockito.given;
//
//@Disabled
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
//class VocServiceTest {
//    @Autowired
//    private VocService vocService;
//    @MockBean
//    private InfluxDBUtil influxDBUtil;
//    @MockBean
//    private InfluxDBResultMapper resultMapper;
//
//    @Test
//    void getVoc() {
//        // given
//        Instant time = Instant.now();
//        String device = "test device";
//        String place = "test place";
//        String topic = "test topic";
//        Integer value = 20;
//
//        Voc voc = new Voc(time, device, place, topic, value);
//
//        given(influxDBUtil.processingQuery(anyString())).willReturn(new QueryResult());
//        given(resultMapper.toPOJO(any(), any())).willReturn(List.of(voc));
//
//        // when
//        Voc resultVoc = vocService.getVoc();
//
//        // then
//        assertAll(
//                () -> assertNotNull(resultVoc),
//                () -> assertEquals(time.plus(9, ChronoUnit.HOURS), resultVoc.getTime()),
//                () -> assertEquals(device, resultVoc.getDevice()),
//                () -> assertEquals(place, resultVoc.getPlace()),
//                () -> assertEquals(topic, resultVoc.getTopic()),
//                () -> assertEquals(value, resultVoc.getValue())
//        );
//    }
//}