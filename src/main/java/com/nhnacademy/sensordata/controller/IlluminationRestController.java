package com.nhnacademy.sensordata.controller;

import com.nhnacademy.sensordata.measurement.illumination.Illumination;
import com.nhnacademy.sensordata.measurement.illumination.IlluminationMaxMin;
import com.nhnacademy.sensordata.measurement.illumination.IlluminationMean;
import com.nhnacademy.sensordata.service.IlluminationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Illumination api controller
 *
 * @author parksangwon
 * @version 1.0.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sensor/illumination")
@Tag(name = "Illumination Rest Controller", description = "조도 조회를 위한 API")
public class IlluminationRestController {
    private final IlluminationService illuminationService;

    /**
     * 가장 최신 조도를 단일로 조회하는 api
     *
     * @return 최신 조도 응답
     */
    @GetMapping
    @Operation(summary = "조도 단일 조회")
    public ResponseEntity<Illumination> getIllumination(@RequestParam String place) {
        Illumination illumination = illuminationService.getIllumination(place);

        return ResponseEntity.ok(illumination);
    }

    /**
     * 당일 0시부터 현재 시간까지 1시간 주기로 조도를 조회하는 api
     *
     * @return 당일 조도 리스트 응답
     */
    @GetMapping("/day")
    @Operation(summary = "조도 일간 조회")
    public ResponseEntity<List<IlluminationMaxMin>> getDailyIlluminations(@RequestParam String place) {
        List<IlluminationMaxMin> illuminations = illuminationService.getDailyIlluminations(place);

        return ResponseEntity.ok(illuminations);
    }

    /**
     * 당일 0시부터 현재 시간까지 1시간 주기로 평균 조도를 조회하는 api
     *
     * @return 당일 조도 리스트 응답
     */
    @GetMapping("/day-mean")
    @Operation(summary = "조도 일간 1시간 집계 조회")
    public ResponseEntity<List<IlluminationMean>> getDailyIlluminationMean(@RequestParam String place) {
        List<IlluminationMean> illuminations = illuminationService.getDailyIlluminationsMean(place);

        return ResponseEntity.ok(illuminations);
    }

    /**
     * 일주일 전부터 오늘까지 하루 단위로 조도를 조회하는 api
     *
     * @return 일주일 조도 리스트 응답
     */
    @GetMapping("/week")
    @Operation(summary = "조도 주간 조회")
    public ResponseEntity<List<IlluminationMaxMin>> getWeeklyIlluminations() {
        List<IlluminationMaxMin> illuminations = illuminationService.getWeeklyIlluminations();

        return ResponseEntity.ok(illuminations);
    }

    /**
     * 한달 전부터 오늘까지 하루 단위로 조도를 조회하는 api
     *
     * @return 한달 조도 리스트 응답
     */
    @GetMapping("/month")
    @Operation(summary = "조도 월간 조회")
    public ResponseEntity<List<IlluminationMaxMin>> getMonthlyIlluminations() {
        List<IlluminationMaxMin> illuminations = illuminationService.getMonthlyIlluminations();

        return ResponseEntity.ok(illuminations);
    }
}
