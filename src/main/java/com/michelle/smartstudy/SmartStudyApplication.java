package com.michelle.smartstudy;

import com.michelle.smartstudy.model.entity.TbCourse;
import com.michelle.smartstudy.mq.consumer.HomeworkConsumerManager;
import com.michelle.smartstudy.mq.consumer.SubmissionConsumerManager;
import com.michelle.smartstudy.service.base.ITbCourseService;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
@MapperScan(value = "com.michelle.smartstudy.mapper")
@Slf4j
public class SmartStudyApplication {

    @Autowired
    private ITbCourseService tbCourseService;

    public static void main(String[] args) {
        SpringApplication.run(SmartStudyApplication.class, args);
    }

    // 服务器启动时，让所有已存在DB中课程的消费者都开始监听课程队列
    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext context) {
        return args -> {
            try{
                HomeworkConsumerManager hwConsumer = context.getBean(HomeworkConsumerManager.class);
                SubmissionConsumerManager submissionConsumer = context.getBean(SubmissionConsumerManager.class);

                // 从数据库中读取所有课程信息,并创建消费者
                List<TbCourse> courses = tbCourseService.list();
                for (TbCourse course : courses) {
                    log.info("课程名称：{}",course.getTitle());
                    String queueName = course.getTitle();
                    hwConsumer.addConsumer(queueName);
                    submissionConsumer.addConsumer(queueName+"sub");
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }

        };
    }

}
