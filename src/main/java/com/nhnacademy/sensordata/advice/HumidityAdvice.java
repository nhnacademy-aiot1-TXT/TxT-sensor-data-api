package com.nhnacademy.sensordata.advice;

import com.nhnacademy.sensordata.controller.HumidityRestController;
import com.nhnacademy.sensordata.dto.ApiExceptionDto;
import com.nhnacademy.sensordata.exception.HumidityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice(basePackageClasses = {HumidityRestController.class})
public class HumidityAdvice {
    @ExceptionHandler(value = {HumidityNotFoundException.class})
    public ResponseEntity<ApiExceptionDto> humidityNotFoundExceptionHandler(HumidityNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiExceptionDto(LocalDateTime.now(), exception.getMessage()));
    }
}
