package com.nhnacademy.sensordata.service;

import com.nhnacademy.sensordata.measurement.voc.Voc;

/**
 * voc 서비스 interface
 *
 * @author parksangwon
 * @version 1.0.0
 */
public interface VocService {
    /**
     * voc 단일 조회 메서드
     *
     * @param place 장소
     * @return 단일 voc
     */
    Voc getVoc(String place);
}
