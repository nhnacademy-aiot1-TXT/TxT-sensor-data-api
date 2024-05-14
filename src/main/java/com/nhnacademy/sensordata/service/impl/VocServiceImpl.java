package com.nhnacademy.sensordata.service.impl;

import com.nhnacademy.sensordata.exception.VocNotFoundException;
import com.nhnacademy.sensordata.measurement.voc.Voc;
import com.nhnacademy.sensordata.service.VocService;
import com.nhnacademy.sensordata.util.InfluxDBUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * voc 서비스 class
 *
 * @author parksangwon
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
public class VocServiceImpl implements VocService {
    private final InfluxDBUtil influxDBUtil;
    private static final String COLLECTION_TYPE = "tvoc";

    /**
     * influxdb에서 최신 voc를 조회 후 반환하는 메서드
     *
     * @param place 장소
     * @return 단일 온도
     */
    @Override
    public Voc getVoc(String place) {
        return influxDBUtil.getSensorData(COLLECTION_TYPE, place, Voc.class)
                .orElseThrow(() -> new VocNotFoundException("voc를 찾을 수 없습니다."));
    }
}
