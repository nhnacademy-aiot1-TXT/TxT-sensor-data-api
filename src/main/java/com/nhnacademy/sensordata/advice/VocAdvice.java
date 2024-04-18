package com.nhnacademy.sensordata.advice;

import com.nhnacademy.sensordata.controller.VocRestController;
import com.nhnacademy.sensordata.dto.ApiExceptionDto;
import com.nhnacademy.sensordata.exception.VocNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

/**
 * VocRestController에서 발생하는 Exception 처리 Advice
 *
 * @author jongsikk
 * @version 1.0.0
 */
@RestControllerAdvice(basePackageClasses = {VocRestController.class})
public class VocAdvice {
    /**
     * VocNotFoundException Handler
     *
     * @param exception VocNotFoundException
     * @return ApiExceptionDto(현재시간, exception message)를 담은 ResponseEntity
     */
    @ExceptionHandler(value = {VocNotFoundException.class})
    public ResponseEntity<ApiExceptionDto> vocNotFoundExceptionHandler(VocNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiExceptionDto(LocalDateTime.now(), exception.getMessage()));
    }
}
