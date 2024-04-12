package com.nhnacademy.sensordata.controller;

import com.nhnacademy.sensordata.entity.co2.Co2;
import com.nhnacademy.sensordata.entity.co2.Co2MaxMinDaily;
import com.nhnacademy.sensordata.entity.co2.Co2MaxMinMonthly;
import com.nhnacademy.sensordata.entity.co2.Co2MaxMinWeekly;
import com.nhnacademy.sensordata.service.Co2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/co2")
public class Co2RestController {
    private final Co2Service co2Service;

    @GetMapping
    public ResponseEntity<Co2> getHumidity() {
        return ResponseEntity.ok(co2Service.getCo2());
    }

    @GetMapping("/day")
    public ResponseEntity<List<Co2MaxMinDaily>> getDailyHumidity() {
        return ResponseEntity.ok(co2Service.getDailyCo2());
    }

    @GetMapping("/week")
    public ResponseEntity<List<Co2MaxMinWeekly>> getWeeklyHumidity() {
        return ResponseEntity.ok(co2Service.getWeeklyCo2());
    }

    @GetMapping("/month")
    public ResponseEntity<List<Co2MaxMinMonthly>> getMonthlyHumidity() {
        return ResponseEntity.ok(co2Service.getMonthlyCo2());
    }
}
