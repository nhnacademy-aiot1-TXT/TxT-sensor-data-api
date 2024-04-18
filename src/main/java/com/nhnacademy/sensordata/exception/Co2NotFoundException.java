package com.nhnacademy.sensordata.exception;

/**
 * co2를 찾을 수 없을 때 발생하는 예외 클래스
 *
 * @author parksangwon
 */
public class Co2NotFoundException extends RuntimeException {
    /**
     * co2 예외 기본 생성자
     */
    public Co2NotFoundException() {
    }

    /**
     * co2 예외에 메시지가 포함되는 생성자
     *
     * @param message 예외 메시지
     */
    public Co2NotFoundException(String message) {
        super(message);
    }
}
