package com.nhnacademy.sensordata.exception;

public class TemperatureNotFoundException extends RuntimeException {
    public TemperatureNotFoundException() {
    }

    public TemperatureNotFoundException(String message) {
        super(message);
    }
}