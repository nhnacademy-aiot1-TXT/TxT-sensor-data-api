package com.nhnacademy.sensordata.controller;

import com.nhnacademy.sensordata.entity.people_count.PeopleCount;
import com.nhnacademy.sensordata.service.PeopleCountService;
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
public class PeopleCountRestController {
    private final PeopleCountService peopleCountService;

    /**
     * 가장 최신 people-count 값 조회 api
     *
     * @return 최신 people-count 응답
     */
    @GetMapping
    public ResponseEntity<PeopleCount> getPeopleCount() {
        return ResponseEntity.ok(peopleCountService.getPeopleCount());
    }
}
