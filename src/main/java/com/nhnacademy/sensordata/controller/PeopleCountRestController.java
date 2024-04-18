package com.nhnacademy.sensordata.controller;

import com.nhnacademy.sensordata.measurement.people_count.PeopleCount;
import com.nhnacademy.sensordata.service.PeopleCountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * people-count api controller
 *
 * @author jongsikk
 * @version 1.0.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/people-count")
@Tag(name = "PeopleCount Rest Controller", description = "people-count 조회를 위한 API")
public class PeopleCountRestController {
    private final PeopleCountService peopleCountService;

    /**
     * 가장 최신 people-count 값 조회 api
     *
     * @return 최신 people-count 응답
     */
    @GetMapping
    @Operation(summary = "단일 people-count(in/out) 조회")
    public ResponseEntity<PeopleCount> getPeopleCount() {
        return ResponseEntity.ok(peopleCountService.getPeopleCount());
    }
}
