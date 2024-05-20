package com.michelle.smartstudy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = "com.michelle.smartstudy.mapper")
public class SmartStudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartStudyApplication.class, args);
    }

}
