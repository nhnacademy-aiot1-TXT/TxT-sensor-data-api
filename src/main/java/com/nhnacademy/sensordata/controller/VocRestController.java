package com.nhnacademy.sensordata.controller;

import com.nhnacademy.sensordata.entity.voc.Voc;
import com.nhnacademy.sensordata.service.VocService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/voc")
public class VocRestController {
    private final VocService vocService;

    @GetMapping
    public ResponseEntity<Voc> getVoc() {
        Voc voc = vocService.getVoc();

        return ResponseEntity.ok(voc);
    }
}
