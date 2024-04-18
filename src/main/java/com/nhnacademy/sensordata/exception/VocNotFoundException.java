package com.nhnacademy.sensordata.exception;

public class VocNotFoundException extends RuntimeException {
    public VocNotFoundException() {
    }

    public VocNotFoundException(String message) {
        super(message);
    }
}
