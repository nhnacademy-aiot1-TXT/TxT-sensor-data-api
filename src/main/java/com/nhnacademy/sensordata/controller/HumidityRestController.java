package com.nhnacademy.sensordata.controller;

import com.nhnacademy.sensordata.entity.Humidity;
import com.nhnacademy.sensordata.entity.HumidityMaxMin;
import com.nhnacademy.sensordata.service.HumidityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/humidity")
@RequiredArgsConstructor
public class HumidityRestController {
    private final HumidityService humidityService;

    @GetMapping
    public ResponseEntity<Humidity> getHumidity() {
        return ResponseEntity.ok(humidityService.getHumidity());
    }

    @GetMapping("/day")
    public ResponseEntity<List<HumidityMaxMin>> getDailyHumidity() {
        return ResponseEntity.ok(humidityService.getDailyHumidity());
    }
}
