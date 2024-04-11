package com.nhnacademy.sensordata.controller;

import com.nhnacademy.sensordata.entity.Temperature;
import com.nhnacademy.sensordata.entity.TemperatureMaxMinDaily;
import com.nhnacademy.sensordata.entity.TemperatureMaxMinWeekly;
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
    public ResponseEntity<List<TemperatureMaxMinDaily>> getDailyTemperatures() {
        List<TemperatureMaxMinDaily> temperatures = temperatureService.getDailyTemperatures();

        return ResponseEntity.ok(temperatures);
    }

    @GetMapping("/week")
    public ResponseEntity<List<TemperatureMaxMinWeekly>> getWeeklyTemperatures() {
        List<TemperatureMaxMinWeekly> temperatures = temperatureService.getWeeklyTemperatures();

        return ResponseEntity.ok(temperatures);
    }
}
