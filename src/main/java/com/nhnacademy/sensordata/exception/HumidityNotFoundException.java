package com.nhnacademy.sensordata.exception;

/**
 * 습도를 찾을 수 없을 때 발생하는 예외 class
 *
 * @author parksangwon
 * @version 1.0.0
 */
public class HumidityNotFoundException extends RuntimeException {
    /**
     * 습도 예외 기본 생성자
     */
    public HumidityNotFoundException() {
    }

    /**
     * 습도 예외에 메시지가 포함되는 생성자
     *
     * @param message 예외 메시지
     */
    public HumidityNotFoundException(String message) {
        super(message);
    }
}
