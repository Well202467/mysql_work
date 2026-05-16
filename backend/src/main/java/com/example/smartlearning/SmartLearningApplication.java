package com.example.smartlearning;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.example.smartlearning.mapper")
@SpringBootApplication
public class SmartLearningApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartLearningApplication.class, args);
    }
}
