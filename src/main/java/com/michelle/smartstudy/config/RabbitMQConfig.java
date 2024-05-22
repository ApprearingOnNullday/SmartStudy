package com.michelle.smartstudy.config;

import lombok.Data;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "rabbitmq")
@Data
public class RabbitMQConfig {

    private String courseExcName = "courseTopicExchange";   // 交换机名称

//    @Bean
//    public TopicExchange topicExchange() {
//        return ExchangeBuilder.topicExchange(courseExcName).build();
//    }

    @Bean
    public DirectExchange directExchange() {
        return ExchangeBuilder.directExchange(courseExcName).build();
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }

}
