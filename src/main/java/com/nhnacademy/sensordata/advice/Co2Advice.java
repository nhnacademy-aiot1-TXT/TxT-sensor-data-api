package com.nhnacademy.sensordata.advice;

import com.nhnacademy.sensordata.dto.ApiExceptionDto;
import com.nhnacademy.sensordata.exception.Co2NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice(basePackageClasses = {Co2NotFoundException.class})
public class Co2Advice {
    @ExceptionHandler(value = Co2NotFoundException.class)
    public ResponseEntity<ApiExceptionDto> co2NotFoundExceptionHandler(Co2NotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiExceptionDto(LocalDateTime.now(), exception.getMessage()));
    }
}
