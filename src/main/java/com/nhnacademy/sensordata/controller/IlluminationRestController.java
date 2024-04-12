package com.nhnacademy.sensordata.controller;

import com.nhnacademy.sensordata.entity.illumination.Illumination;
import com.nhnacademy.sensordata.service.IlluminationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/illumination")
public class IlluminationRestController {
    private final IlluminationService illuminationService;

    @GetMapping
    public ResponseEntity<Illumination> getIllumination() {
        Illumination illumination = illuminationService.getIllumination();

        return ResponseEntity.ok(illumination);
    }
}
