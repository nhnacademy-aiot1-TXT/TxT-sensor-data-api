package com.nhnacademy.sensordata.advice;

import com.nhnacademy.sensordata.controller.PeopleCountRestController;
import com.nhnacademy.sensordata.dto.ApiExceptionDto;
import com.nhnacademy.sensordata.exception.PeopleCountNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice(basePackageClasses = {PeopleCountRestController.class})
public class PeopleCountAdvice {
    @ExceptionHandler(value = {PeopleCountNotFoundException.class})
    public ResponseEntity<ApiExceptionDto> peopleCountNotFoundExceptionHandler(PeopleCountNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiExceptionDto(LocalDateTime.now(), exception.getMessage()));
    }
}
