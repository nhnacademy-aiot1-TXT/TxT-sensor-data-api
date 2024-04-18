package com.nhnacademy.sensordata.advice;

import com.nhnacademy.sensordata.controller.TemperatureRestController;
import com.nhnacademy.sensordata.dto.ApiExceptionDto;
import com.nhnacademy.sensordata.exception.TemperatureNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

/**
 * TemperatureRestController에서 발생하는 Exception 처리 Advice
 *
 * @author jongsikk
 * @version 1.0.0
 */
@RestControllerAdvice(basePackageClasses = {TemperatureRestController.class})
public class TemperatureAdvice {
    /**
     * TemperatureNotFoundException Handler
     *
     * @param exception TemperatureNotFoundException
     * @return ApiExceptionDto(현재시간, exception message)를 담은 ResponseEntity
     */
    @ExceptionHandler(value = {TemperatureNotFoundException.class})
    public ResponseEntity<ApiExceptionDto> temperatureNotFoundExceptionHandler(TemperatureNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiExceptionDto(LocalDateTime.now(), exception.getMessage()));
    }
}
