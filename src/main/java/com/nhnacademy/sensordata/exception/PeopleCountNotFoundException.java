package com.nhnacademy.sensordata.exception;

/**
 * people count(in/out)를 찾을 수 없을 때 발생하는 예외 class
 *
 * @author parksangwon
 * @version 1.0.0
 */
public class PeopleCountNotFoundException extends RuntimeException {
    /**
     * people count(in/out) 예외 기본 생성자
     */
    public PeopleCountNotFoundException() {
    }

    /**
     * people count(in/out) 예외에 메시지가 포함되는 생성자
     *
     * @param message 예외 메시지
     */
    public PeopleCountNotFoundException(String message) {
        super(message);
    }
}
