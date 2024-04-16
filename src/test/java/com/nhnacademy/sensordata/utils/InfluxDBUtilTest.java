//package com.nhnacademy.sensordata.utils;
//
//import org.influxdb.dto.Point;
//import org.influxdb.dto.QueryResult;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.influxdb.InfluxDBTemplate;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//
//@Disabled
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
//class InfluxDBUtilTest {
//    @Autowired
//    private InfluxDBUtil influxDBUtil;
//    @MockBean(name = "influxDBTemplate")
//    private InfluxDBTemplate<Point> influxDBTemplate;
//
//    @Test
//    void testProcessingQuery() {
//        // given
//        String queryString = "SELECT * FROM measurement";
//        QueryResult queryResult = new QueryResult();
//
//        given(influxDBTemplate.query(any())).willReturn(queryResult);
//
//        // when
//        QueryResult result = influxDBUtil.processingQuery(queryString);
//
//        // then
//        Assertions.assertEquals(queryResult, result);
//    }
//}
