package com.nhnacademy.sensordata.controller;

import com.nhnacademy.sensordata.entity.illumination.Illumination;
import com.nhnacademy.sensordata.entity.illumination.IlluminationMaxMinDaily;
import com.nhnacademy.sensordata.entity.illumination.IlluminationMaxMinWeekly;
import com.nhnacademy.sensordata.service.IlluminationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @GetMapping("/day")
    public ResponseEntity<List<IlluminationMaxMinDaily>> getDailyIlluminations() {
        List<IlluminationMaxMinDaily> illuminations = illuminationService.getDailyIlluminations();

        return ResponseEntity.ok(illuminations);
    }

    @GetMapping("/week")
    public ResponseEntity<List<IlluminationMaxMinWeekly>> getWeeklyIlluminations() {
        List<IlluminationMaxMinWeekly> illuminations = illuminationService.getWeeklyIlluminations();

        return ResponseEntity.ok(illuminations);
    }
}
