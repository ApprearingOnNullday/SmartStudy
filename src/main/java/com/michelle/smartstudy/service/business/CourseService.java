package com.michelle.smartstudy.service.business;

import com.michelle.smartstudy.model.dto.UserDTO;
import com.michelle.smartstudy.model.entity.TbCourse;
import com.michelle.smartstudy.model.enums.RoleEnum;
import com.michelle.smartstudy.model.query.CourseAddQuery;
import com.michelle.smartstudy.model.vo.BaseVO;
import com.michelle.smartstudy.mq.consumer.HomeworkConsumerManager;
import com.michelle.smartstudy.service.base.ITbCourseService;
import com.michelle.smartstudy.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CourseService {

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private TopicExchange topicExchange;

    @Autowired
    private HomeworkConsumerManager hwConsumer;

    @Autowired
    private ITbCourseService tbCourseService;

    /**
     * 添加（创建）课程，创建以课程名命名的消息队列，并连接到交换机courseTopicExchange
     * 将routing-key也设置为课程名称
     */
    public BaseVO<Object> addCourse(CourseAddQuery courseAddQuery) {
        BaseVO<Object> baseVO = new BaseVO<>();
        // 获取当前登录用户
        UserDTO user = UserHolder.get();
        // 验证角色是否为教师
        if(RoleEnum.TEACHER.getCode().equals(user.getRole())) {
            // 课程名称
            String courseName = courseAddQuery.getTitle();
            // 课程描述
            String description = courseAddQuery.getDescription();
            // 教师id
            Integer teacherId = user.getId();
            // 设置课程相关信息，存入数据库表
            TbCourse tbCourse = TbCourse.builder()
                    .title(courseName)
                    .teacherId(teacherId)
                    .description(description)
                    .build();
            tbCourseService.save(tbCourse);

            // 创建队列
            Queue queue = new Queue(courseName);
            rabbitAdmin.declareQueue(queue);
            // 绑定队列到交换机
            Binding binding = BindingBuilder.bind(queue).to(topicExchange).with(courseName);
            rabbitAdmin.declareBinding(binding);

            log.info("Course " + courseName + " added successfully!");

            // 创建该队列的消费者，并让其监听队列
            hwConsumer.addConsumer(courseName);
            return baseVO.success();
        } else {    // 若不为教师角色
            return baseVO.failure().setData("不为教师角色，无法创建课程！");
        }
    }

    /**
     * 删除课程，删除rabbitMQ中的队列，删除监听该队列的消费者
     */
    public BaseVO<Object> deleteCourse(Integer courseId) {
        BaseVO<Object> baseVO = new BaseVO<>();
        // 获取当前登录用户，验证身份
        UserDTO user = UserHolder.get();
        if(RoleEnum.TEACHER.getCode().equals(user.getRole())) {
            // 获取course Entity
            TbCourse tbCourse = tbCourseService.getById(courseId);
            // 获取课程名称（对应rabbit中的队列名称）
            String queueName = tbCourse.getTitle();
            // 删除课程 todo:后续考虑一下删除course会对DB中其它表带来什么影响 没时间就不管了
            tbCourseService.removeById(courseId);
            // 删除该课程在rabbitMQ中对应的队列 & 删除监听该队列的消费者
            hwConsumer.removeConsumer(queueName);
            return baseVO.success();
        } else {    // 若不为教师角色
            return baseVO.failure().setData("不为教师角色，无法删除课程！");
        }
    }
}
