package com.nhnacademy.sensordata.exception;

/**
 * voc를 찾을 수 없을 때 발생하는 예외 class
 *
 * @author parksangwon
 * @version 1.0.0
 */
public class VocNotFoundException extends RuntimeException {
    /**
     * voc 예외 기본 생성자
     */
    public VocNotFoundException() {
    }

    /**
     * voc 예외에 메시지가 포함되는 생성자
     *
     * @param message 예외 메시지
     */
    public VocNotFoundException(String message) {
        super(message);
    }
}
