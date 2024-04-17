package com.nhnacademy.sensordata.controller;

import com.nhnacademy.sensordata.entity.co2.Co2;
import com.nhnacademy.sensordata.entity.co2.Co2MaxMinDaily;
import com.nhnacademy.sensordata.entity.co2.Co2MaxMinMonthly;
import com.nhnacademy.sensordata.entity.co2.Co2MaxMinWeekly;
import com.nhnacademy.sensordata.service.Co2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * co2 api controller
 *
 * @author jongsikk
 * @version 1.0.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/co2")
public class Co2RestController {
    private final Co2Service co2Service;

    /**
     * 가장 최신 co2 값 조회 api
     *
     * @return 최신 co2 응답
     */
    @GetMapping
    public ResponseEntity<Co2> getHumidity() {
        return ResponseEntity.ok(co2Service.getCo2());
    }

    /**
     * 일별(00시 ~ 현재시간) 1시간 간격 co2 값 조회 api
     *
     * @return 시간별 co2 list 응답
     */
    @GetMapping("/day")
    public ResponseEntity<List<Co2MaxMinDaily>> getDailyHumidity() {
        return ResponseEntity.ok(co2Service.getDailyCo2());
    }

    /**
     * 주별(일주일간 1일 간격) co2 값 조회 api
     *
     * @return 일별 co2 list 응답
     */
    @GetMapping("/week")
    public ResponseEntity<List<Co2MaxMinWeekly>> getWeeklyHumidity() {
        return ResponseEntity.ok(co2Service.getWeeklyCo2());
    }

    /**
     * 월별(한달간 1일 간격) co2 값 조회 api
     *
     * @return 최신 co2 응답
     */
    @GetMapping("/month")
    public ResponseEntity<List<Co2MaxMinMonthly>> getMonthlyHumidity() {
        return ResponseEntity.ok(co2Service.getMonthlyCo2());
    }
}
