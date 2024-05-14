package com.nhnacademy.sensordata.controller;

import com.nhnacademy.sensordata.measurement.temperature.Temperature;
import com.nhnacademy.sensordata.measurement.temperature.TemperatureMaxMin;
import com.nhnacademy.sensordata.measurement.temperature.TemperatureMean;
import com.nhnacademy.sensordata.service.TemperatureService;
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
 * Temperature api controller
 *
 * @author parksangwon
 * @version 1.0.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sensor/temperature")
@Tag(name = "Temperature Rest Controller", description = "온도 조회를 위한 API")
public class TemperatureRestController {
    private final TemperatureService temperatureService;

    /**
     * 가장 최신 온도를 단일로 조회하는 api
     *
     * @return 최신 온도 응답
     */
    @GetMapping
    @Operation(summary = "온도 단일 조회")
    public ResponseEntity<Temperature> getTemperature() {
        Temperature temperature = temperatureService.getTemperature();

        return ResponseEntity.ok(temperature);
    }

    /**
     * 당일 0시부터 현재 시간까지 1시간 주기로 온도를 조회하는 api
     *
     * @return 당일 온도 리스트 응답
     */
    @GetMapping("/day")
    @Operation(summary = "온도 일간 조회")
    public ResponseEntity<List<TemperatureMaxMin>> getDailyTemperatures(@RequestParam String place) {
        List<TemperatureMaxMin> temperatures = temperatureService.getDailyTemperatures(place);

        return ResponseEntity.ok(temperatures);
    }

    /**
     * 당일 0시부터 현재 시간까지 1시간 주기로 평균 온도를 조회하는 api
     *
     * @return 당일 온도 리스트 응답
     */
    @GetMapping("/day-mean")
    @Operation(summary = "온도 일간 1시간 집계 조회")
    public ResponseEntity<List<TemperatureMean>> getDailyTemperaturesMean(@RequestParam String place) {
        List<TemperatureMean> temperatures = temperatureService.getDailyTemperaturesMean(place);

        return ResponseEntity.ok(temperatures);
    }

    /**
     * 일주일 전부터 오늘까지 하루 단위로 온도를 조회하는 api
     *
     * @return 일주일 온도 리스트 응답
     */
    @GetMapping("/week")
    @Operation(summary = "온도 주간 조회")
    public ResponseEntity<List<TemperatureMaxMin>> getWeeklyTemperatures() {
        List<TemperatureMaxMin> temperatures = temperatureService.getWeeklyTemperatures();

        return ResponseEntity.ok(temperatures);
    }

    /**
     * 한달 전부터 오늘까지 하루 단위로 온도를 조회하는 api
     *
     * @return 한달 온도 리스트 응답
     */
    @GetMapping("/month")
    @Operation(summary = "온도 월간 조회")
    public ResponseEntity<List<TemperatureMaxMin>> getMonthlyTemperatures() {
        List<TemperatureMaxMin> temperatures = temperatureService.getMonthlyTemperatures();

        return ResponseEntity.ok(temperatures);
    }
}
