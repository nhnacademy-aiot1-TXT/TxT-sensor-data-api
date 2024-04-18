package com.nhnacademy.sensordata.exception;

public class Co2NotFoundException extends RuntimeException {
    public Co2NotFoundException() {
    }

    public Co2NotFoundException(String message) {
        super(message);
    }
}
