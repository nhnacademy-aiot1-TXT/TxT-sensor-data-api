package com.nhnacademy.sensordata.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 예외 발생 시간과 메시지가 저장되는 Dto class
 */
@Data
public class ApiExceptionDto {
    private final LocalDateTime time;
    private final String message;
}
