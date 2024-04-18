package com.nhnacademy.sensordata.advice;

import com.nhnacademy.sensordata.controller.IlluminationRestController;
import com.nhnacademy.sensordata.dto.ApiExceptionDto;
import com.nhnacademy.sensordata.exception.IlluminationNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice(basePackageClasses = {IlluminationRestController.class})
public class IlluminationAdvice {
    @ExceptionHandler(value = {IlluminationNotFoundException.class})
    public ResponseEntity<ApiExceptionDto> temperatureNotFoundExceptionHandler(IlluminationNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiExceptionDto(LocalDateTime.now(), exception.getMessage()));
    }
}
