package com.nhnacademy.sensordata.controller;

import com.nhnacademy.sensordata.entity.people_count.PeopleCount;
import com.nhnacademy.sensordata.service.PeopleCountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/people-count")
public class PeopleCountRestController {
    private final PeopleCountService peopleCountService;

    @GetMapping
    public ResponseEntity<PeopleCount> getPeopleCount() {
        return ResponseEntity.ok(peopleCountService.getPeopleCount());
    }
}
