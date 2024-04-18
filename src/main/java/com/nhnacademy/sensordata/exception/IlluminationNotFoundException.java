package com.nhnacademy.sensordata.exception;

public class IlluminationNotFoundException extends RuntimeException {
    public IlluminationNotFoundException() {
    }

    public IlluminationNotFoundException(String message) {
        super(message);
    }
}
