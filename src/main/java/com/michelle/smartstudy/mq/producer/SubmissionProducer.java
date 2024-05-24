package com.michelle.smartstudy.mq.producer;

import com.alibaba.fastjson2.JSON;
import com.michelle.smartstudy.config.RabbitMQConfig;
import com.michelle.smartstudy.model.dto.SubmissionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class SubmissionProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMQConfig rabbitMQConfig;

    public void sendMsg(String queueName, SubmissionDTO submissionDTO) {
        String courseExcName = rabbitMQConfig.getCourseExcName();
        Message message = new Message(JSON.toJSONString(submissionDTO).getBytes());
        rabbitTemplate.convertAndSend(courseExcName, queueName, message);
        log.info("向交换机{}发送消息{}，时间是{}", courseExcName, message, new Date());
    }

}
