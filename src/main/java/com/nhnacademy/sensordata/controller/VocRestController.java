package com.nhnacademy.sensordata.controller;

import com.nhnacademy.sensordata.entity.voc.Voc;
import com.nhnacademy.sensordata.service.VocService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Temperature api controller
 *
 * @author parksangwon
 * @version 1.0.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/voc")
public class VocRestController {
    private final VocService vocService;

    /**
     * 가장 최신 voc를 단일로 조회하는 api
     *
     * @return 최신 온도 응답
     */
    @GetMapping
    public ResponseEntity<Voc> getVoc() {
        Voc voc = vocService.getVoc();

        return ResponseEntity.ok(voc);
    }
}
