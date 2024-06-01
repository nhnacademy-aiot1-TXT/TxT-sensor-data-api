package com.nhnacademy.sensordata.controller;

import com.nhnacademy.sensordata.measurement.co2.Co2;
import com.nhnacademy.sensordata.measurement.co2.Co2MaxMin;
import com.nhnacademy.sensordata.measurement.co2.Co2Mean;
import com.nhnacademy.sensordata.service.Co2Service;
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
 * co2 api controller
 *
 * @author jongsikk
 * @version 1.0.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sensor/co2")
@Tag(name = "Co2 Rest Controller", description = "Co2 조회를 위한 API")
public class Co2RestController {
    private final Co2Service co2Service;

    /**
     * 가장 최신 co2 값 조회 api
     *
     * @param place 장소
     * @return 최신 co2 응답
     */
    @GetMapping
    @Operation(summary = "단일 Co2 조회")
    public ResponseEntity<Co2> getCo2(@RequestParam String place) {
        return ResponseEntity.ok(co2Service.getCo2(place));
    }

    /**
     * 일별(00시 ~ 현재시간) 1시간 간격 co2 값 조회 api
     *
     * @param place 장소
     * @return 시간별 co2 list 응답
     */
    @GetMapping("/day")
    @Operation(summary = "일별 Co2 조회")
    public ResponseEntity<List<Co2MaxMin>> getDailyCo2(@RequestParam String place) {
        return ResponseEntity.ok(co2Service.getDailyCo2(place));
    }

    /**
     * 일별(00시 ~ 현재시간) 1시간 간격 평균 co2 값 조회 api
     *
     * @param place 장소
     * @return 시간별 co2 평균 list 응답
     */
    @GetMapping("/day-mean")
    @Operation(summary = "일별 co2 시간별 조회")
    public ResponseEntity<List<Co2Mean>> getDailyMeanCo2(@RequestParam String place) {
        return ResponseEntity.ok(co2Service.getDailyMeanCo2(place));
    }

    /**
     * 주별(일주일간 1일 간격) co2 값 조회 api
     *
     * @param place 장소
     * @return 일별 co2 list 응답
     */
    @GetMapping("/week")
    @Operation(summary = "주별 Co2 조회")
    public ResponseEntity<List<Co2MaxMin>> getWeeklyCo2(@RequestParam String place) {
        return ResponseEntity.ok(co2Service.getWeeklyCo2(place));
    }

    /**
     * 월별(한달간 1일 간격) co2 값 조회 api
     *
     * @param place 장소
     * @return 최신 co2 응답
     */
    @GetMapping("/month")
    @Operation(summary = "월별 Co2 조회")
    public ResponseEntity<List<Co2MaxMin>> getMonthlyCo2(@RequestParam String place) {
        return ResponseEntity.ok(co2Service.getMonthlyCo2(place));
    }
}
