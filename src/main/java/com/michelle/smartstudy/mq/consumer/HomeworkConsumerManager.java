package com.michelle.smartstudy.mq.consumer;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.michelle.smartstudy.model.dto.HomeworkDTO;
import com.michelle.smartstudy.model.entity.TbHomework;
import com.michelle.smartstudy.model.entity.TbHomeworkRead;
import com.michelle.smartstudy.model.entity.TbStudentCourse;
import com.michelle.smartstudy.model.enums.ReadStatusEnum;
import com.michelle.smartstudy.service.base.ITbHomeworkReadService;
import com.michelle.smartstudy.service.base.ITbHomeworkService;
import com.michelle.smartstudy.service.base.ITbStudentCourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class HomeworkConsumerManager {

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Autowired
    private ITbHomeworkService tbHomeworkService;

    @Autowired
    private ITbStudentCourseService tbStudentCourseService;

    @Autowired
    private ITbHomeworkReadService tbHomeworkReadService;

    private final Map<String, SimpleMessageListenerContainer> listenerContainers = new ConcurrentHashMap<>();

//    private static Integer msgNum = 0;

    // 动态生成对应队列的Consumer（每个课程都有一个对应的queue和与其对应的consumer）
    public void addConsumer(String queueName) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);  // 设置连接工厂
        container.setQueueNames(queueName); // 指定监听的队列名称
        container.setMessageListener(new MessageListenerAdapter(new Object() {
            // 在收到对应队列中的消息时会执行handleMessage方法
            public void handleMessage(byte[] body) {
//                msgNum++;
                // 根据队列名称进行操作(只是为了说明可以获取到队列名称)
                log.info("Received homework for " + queueName);
                // 检查收到的内容
                if (body == null || body.length == 0) {
                    log.error("null msg");
                } else {    // 信息不为空
                    String msg = new String(body);
                    log.info("receive message {}", msg);

                    // 解析信息为HomeworkDTO Entity
                    HomeworkDTO homeworkDTO = JSON.parseObject(msg, HomeworkDTO.class);
//                    System.out.println(homeworkDTO);
                    // 从HomeworkDTO中获取作业对应的课程id
                    Integer courseId = homeworkDTO.getCourseId();   // 课程id
                    // 创建TbHomework，并插入DB中
                    TbHomework tbHomework = TbHomework.builder()
                            .title(homeworkDTO.getTitle())
                            .courseId(courseId)
                            .description(homeworkDTO.getDescription())
                            .start(homeworkDTO.getStart())
                            .end(homeworkDTO.getEnd())
                            .build();
                    // 存入tb_homework表中
                    tbHomeworkService.save(tbHomework);
                    // 获取homework_id
                    Integer homeworkId = tbHomework.getId();
                    // 向tb_homework_read中存入相关信息
                    // 1. 到tb_student_course表中，根据课程id找到对应的学生id的list
                    QueryWrapper<TbStudentCourse> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("course_id", courseId);
                    List<Integer> studentIds = tbStudentCourseService.list(queryWrapper).stream().
                            map(TbStudentCourse::getStudentId).toList();
                    // 2. 在tb_homework_read表中依次插入信息，并设置status为2（未完成）
                    for(Integer studentId: studentIds) {
                        TbHomeworkRead homeworkRead = TbHomeworkRead.builder()
                                .studentId(studentId)
                                .homeworkId(homeworkId)
                                .status(ReadStatusEnum.UNFINISHED.getCode())
                                .build();
                        tbHomeworkReadService.save(homeworkRead);
                    }
                }
                // 这里可以添加更多基于队列名称的逻辑
            }
            /*
            // 在 handleMessage 方法内部，可以显式调用底下这些方法这些辅助方法来处理消息。
            // 定义其他方法
            private void processHomework(String homework) {
                // 解析作业内容或其他处理逻辑
                System.out.println("Processing homework: " + homework);
                // 假设进行一些处理
                String result = analyzeHomework(homework);
                System.out.println("Homework analysis result: " + result);
            }

            private String analyzeHomework(String homework) {
                // 模拟作业分析
                return "Analysis of " + homework;
            } */
        }));
        container.start();  // 启动监听容器
        // 将Container保存到map
        listenerContainers.put(queueName, container);
    }

    // 在教师删除课程时，删除rabbitMQ上对应的队列，并删除监听该队列的消费者
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
