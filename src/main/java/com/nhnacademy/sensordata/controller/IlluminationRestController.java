package com.nhnacademy.sensordata.controller;

import com.nhnacademy.sensordata.entity.illumination.Illumination;
import com.nhnacademy.sensordata.entity.illumination.IlluminationMaxMinDaily;
import com.nhnacademy.sensordata.entity.illumination.IlluminationMaxMinMonthly;
import com.nhnacademy.sensordata.entity.illumination.IlluminationMaxMinWeekly;
import com.nhnacademy.sensordata.service.IlluminationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping("/api/illumination")
public class IlluminationRestController {
    private final IlluminationService illuminationService;

    /**
     * 가장 최신 조도를 단일로 조회하는 api
     *
     * @return 최신 조도 응답
     */
    @GetMapping
    public ResponseEntity<Illumination> getIllumination() {
        Illumination illumination = illuminationService.getIllumination();

        return ResponseEntity.ok(illumination);
    }

    /**
     * 당일 0시부터 현재 시간까지 1시간 주기로 조도를 조회하는 api
     *
     * @return 당일 조도 리스트 응답
     */
    @GetMapping("/day")
    public ResponseEntity<List<IlluminationMaxMinDaily>> getDailyIlluminations() {
        List<IlluminationMaxMinDaily> illuminations = illuminationService.getDailyIlluminations();

        return ResponseEntity.ok(illuminations);
    }

    /**
     * 일주일 전부터 오늘까지 하루 단위로 조도를 조회하는 api
     *
     * @return 일주일 조도 리스트 응답
     */
    @GetMapping("/week")
    public ResponseEntity<List<IlluminationMaxMinWeekly>> getWeeklyIlluminations() {
        List<IlluminationMaxMinWeekly> illuminations = illuminationService.getWeeklyIlluminations();

        return ResponseEntity.ok(illuminations);
    }

    /**
     * 한달 전부터 오늘까지 하루 단위로 조도를 조회하는 api
     *
     * @return 한달 조도 리스트 응답
     */
    @GetMapping("/month")
    public ResponseEntity<List<IlluminationMaxMinMonthly>> getMonthlyIlluminations() {
        List<IlluminationMaxMinMonthly> illuminations = illuminationService.getMonthlyIlluminations();

        return ResponseEntity.ok(illuminations);
    }
}
