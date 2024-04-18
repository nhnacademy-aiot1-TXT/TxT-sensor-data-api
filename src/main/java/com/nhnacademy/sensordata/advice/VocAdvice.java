package com.nhnacademy.sensordata.advice;

import com.nhnacademy.sensordata.controller.VocRestController;
import com.nhnacademy.sensordata.dto.ApiExceptionDto;
import com.nhnacademy.sensordata.exception.VocNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice(basePackageClasses = {VocRestController.class})
public class VocAdvice {
    @ExceptionHandler(value = {VocNotFoundException.class})
    public ResponseEntity<ApiExceptionDto> vocNotFoundExceptionHandler(VocNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiExceptionDto(LocalDateTime.now(), exception.getMessage()));
    }
}
