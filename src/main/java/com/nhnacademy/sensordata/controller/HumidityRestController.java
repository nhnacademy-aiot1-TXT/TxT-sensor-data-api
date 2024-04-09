package com.nhnacademy.sensordata.controller;

import com.nhnacademy.sensordata.entity.Humidity;
import com.nhnacademy.sensordata.service.HumidityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/humidity")
@RequiredArgsConstructor
public class HumidityRestController {
    private final HumidityService humidityService;

    @GetMapping
    public Humidity getHumidity() {
        return humidityService.getHumidity();
    }
}
