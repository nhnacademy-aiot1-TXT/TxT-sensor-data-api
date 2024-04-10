package com.nhnacademy.sensordata.controller;

import com.nhnacademy.sensordata.entity.Temperature;
import com.nhnacademy.sensordata.entity.TemperatureMaxMin;
import com.nhnacademy.sensordata.service.TemperatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/temperature")
public class TemperatureRestController {
    private final TemperatureService temperatureService;

    @GetMapping
    public ResponseEntity<Temperature> getTemperature() {
        Temperature temperature = temperatureService.getTemperature();

        return ResponseEntity.ok(temperature);
    }

    @GetMapping("/day")
    public ResponseEntity<List<TemperatureMaxMin>> getDailyTemperatures() {
        List<TemperatureMaxMin> temperatures = temperatureService.getDailyTemperatures();

        return ResponseEntity.ok(temperatures);
    }
}
