package com.nhnacademy.sensordata.controller;

import com.nhnacademy.sensordata.entity.Temperature;
import com.nhnacademy.sensordata.service.TemperatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/temperature")
public class TemperatureRestController {
    private final TemperatureService temperatureService;

    @GetMapping
    public Temperature getTemperature() {
        return temperatureService.getTemperature();
    }
}
