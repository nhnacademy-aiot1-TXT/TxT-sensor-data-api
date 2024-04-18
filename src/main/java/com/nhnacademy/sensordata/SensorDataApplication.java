package com.nhnacademy.sensordata;

import com.nhnacademy.sensordata.properties.PropertiesBase;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackageClasses = PropertiesBase.class)
public class SensorDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(SensorDataApplication.class, args);
    }

}
