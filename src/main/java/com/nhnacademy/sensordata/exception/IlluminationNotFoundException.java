package com.nhnacademy.sensordata.exception;

/**
 * 조도를 찾을 수 없을 때 발생하는 예외 class
 *
 * @author parksangwon
 * @version 1.0.0
 */
public class IlluminationNotFoundException extends RuntimeException {
    /**
     * 조도 예외 기본 생성자
     */
    public IlluminationNotFoundException() {
    }

    /**
     * 조도 예외에 메시지가 포함되는 생성자
     *
     * @param message the message
     */
    public IlluminationNotFoundException(String message) {
        super(message);
    }
}
