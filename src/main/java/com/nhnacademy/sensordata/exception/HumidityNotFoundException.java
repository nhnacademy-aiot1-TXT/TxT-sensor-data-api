package com.nhnacademy.sensordata.exception;

public class HumidityNotFoundException extends RuntimeException {
    public HumidityNotFoundException() {
    }

    public HumidityNotFoundException(String message) {
        super(message);
    }
}
