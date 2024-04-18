package com.nhnacademy.sensordata.exception;

public class PeopleCountNotFoundException extends RuntimeException {
    public PeopleCountNotFoundException() {
    }

    public PeopleCountNotFoundException(String message) {
        super(message);
    }
}
