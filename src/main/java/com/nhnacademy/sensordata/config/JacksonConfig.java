package com.nhnacademy.sensordata.config;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * JacksonConfig 클래스
 * json에서 시간을 파싱하기 위해 설정
 *
 * @author parksangwon
 * @version 1.0.0
 */
@Configuration
public class JacksonConfig {
    /**
     * Java time module java time module.
     *
     * @return the java time module
     */
    @Bean
    public JavaTimeModule javaTimeModule() {
        return new JavaTimeModule();
    }
}