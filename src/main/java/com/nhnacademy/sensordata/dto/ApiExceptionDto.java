package com.nhnacademy.sensordata.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 예외 발생 시간과 메시지가 저장되는 Dto class
 */
@Getter
@Setter
@AllArgsConstructor
public class ApiExceptionDto {
    private final LocalDateTime time;
    private final String message;
}
