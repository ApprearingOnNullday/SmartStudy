package com.michelle.smartstudy.mq.consumer;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.michelle.smartstudy.model.dto.SubmissionDTO;
import com.michelle.smartstudy.model.entity.TbHomeworkRead;
import com.michelle.smartstudy.model.entity.TbSubmission;
import com.michelle.smartstudy.model.enums.CorrectingStatusEnum;
import com.michelle.smartstudy.model.enums.ReadStatusEnum;
import com.michelle.smartstudy.service.base.ITbHomeworkReadService;
import com.michelle.smartstudy.service.base.ITbSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class SubmissionConsumerManager {

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Autowired
    private ITbSubmissionService tbSubmissionService;

    @Autowired
    private ITbHomeworkReadService tbHomeworkReadService;

    private final Map<String, SimpleMessageListenerContainer> listenerContainers = new ConcurrentHashMap<>();

    private static Integer msgNum = 0;

    private static final Integer LIMIT = 3;     // 该队列累计收到多少作业后会给任课老师发邮件

    // 动态生成对应队列的Consumer（每个课程都有一个对应的submissionqueue和与其对应的consumer）
    public void addConsumer(String queueName) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);  // 设置连接工厂
        container.setQueueNames(queueName); // 指定监听的队列名称
        container.setMessageListener(new MessageListenerAdapter(new Object() {
            // 在收到对应队列中的消息时会执行handleMessage方法
            public void handleMessage(byte[] body) {
                msgNum++;
                // 根据队列名称进行操作(只是为了说明可以获取到队列名称)
                log.info("Received submission for " + queueName + ", msgNum: {}", msgNum);
                // 当收到足够多的submission时，可以给老师发邮件提醒批改作业（大概体现了MQ的异步）
                if(msgNum.equals(LIMIT)) {
                    // todo：发邮件
                    msgNum = 0;     // 重置
                }
                // 检查收到的内容
                if (body == null || body.length == 0) {
                    log.error("null msg");
                } else {    // 信息不为空
                    String msg = new String(body);
                    log.info("receive message {}", msg);

                    // 解析信息为SubmissionDTO
                    SubmissionDTO submissionDTO = JSON.parseObject(msg, SubmissionDTO.class);
//                    System.out.println(submissionDTO);
                    // 从SubmissionDTO中获取提交对应的作业id
                    Integer homeworkId = submissionDTO.getHomeworkId();   // 作业id
                    // 创建TbSubmission，并插入DB中
                    TbSubmission submission = TbSubmission.builder()
                            .studentId(submissionDTO.getStudentId())
                            .homeworkId(homeworkId)
                            .submitTime(submissionDTO.getSubmitTime())
                            .content(submissionDTO.getContent())
                            .status(CorrectingStatusEnum.UNCORRECTED.getCode())
                            .build();
                    // 存入tb_submission表中
                    tbSubmissionService.save(submission);
                    // 将tb_homework_read对应的表项状态update为1--已完成
                    TbHomeworkRead record = tbHomeworkReadService.getOne(new QueryWrapper<TbHomeworkRead>()
                            .eq("student_id", submissionDTO.getStudentId())
                            .eq("homework_id", submissionDTO.getHomeworkId()));
                    if(record == null) {
                        log.error("不存在该条作业记录");
                    } else {
                        // 更新该学生的该条作业为已完成状态
                        record.setStatus(ReadStatusEnum.FINISTHED.getCode());
                        tbHomeworkReadService.updateById(record);
                    }
                }
                // 这里可以添加更多基于队列名称的逻辑
            }// handleMessage
        }));
        container.start();  // 启动监听容器
        // 将Container保存到map
        listenerContainers.put(queueName, container);
    }

    // 在教师删除课程时，删除rabbitMQ上对应的submission队列，并删除监听该队列的消费者
    public void removeConsumer(String queueName) {
        SimpleMessageListenerContainer container = listenerContainers.remove(queueName);
        // 删除consumer
        if (container != null) {
            container.stop();
            System.out.println("Consumer for queue " + queueName + " stopped.");
        }

        // 删除队列
        amqpAdmin.deleteQueue(queueName);
        System.out.println("Queue " + queueName + " deleted.");
    }

}
