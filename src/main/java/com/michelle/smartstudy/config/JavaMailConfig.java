package com.michelle.smartstudy.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "mail")
public class JavaMailConfig {

    private String host = "smtp.126.com";
    private String username = "studyserver1052@126.com";
    private String password = "AFJSTAOXWYIIEHVA";
    private String from = "studyserver1052@126.com";

}
