package com.michelle.smartstudy.service.business;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Maps;
import com.michelle.smartstudy.model.dto.UserDTO;
import com.michelle.smartstudy.model.entity.*;
import com.michelle.smartstudy.model.enums.RoleEnum;
import com.michelle.smartstudy.model.query.CourseAddQuery;
import com.michelle.smartstudy.model.vo.BaseVO;
import com.michelle.smartstudy.model.vo.ChosenCourseInfo4StudentsVO;
import com.michelle.smartstudy.model.vo.CourseInfo4StudentsVO;
import com.michelle.smartstudy.mq.consumer.HomeworkConsumerManager;
import com.michelle.smartstudy.mq.consumer.SubmissionConsumerManager;
import com.michelle.smartstudy.service.base.*;
import com.michelle.smartstudy.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CourseService {

    @Autowired
    private RabbitAdmin rabbitAdmin;

//    @Autowired
//    private TopicExchange topicExchange;

    @Autowired
    private DirectExchange directExchange;

    @Autowired
    private HomeworkConsumerManager hwConsumer;

    @Autowired
    private SubmissionConsumerManager submissionConsumer;

    @Autowired
    private ITbCourseService tbCourseService;

    @Autowired
    private ITbUserService tbUserService;

    @Autowired
    private ITbStudentCourseService tbStudentCourseService;

    @Autowired
    private ITbHomeworkService tbHomeworkService;

    @Autowired
    private ITbHomeworkReadService tbHomeworkReadService;

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

            // 创建作业队列
            Queue queue = new Queue(courseName);
            rabbitAdmin.declareQueue(queue);
            // 绑定队列到交换机
            Binding binding = BindingBuilder.bind(queue).to(directExchange).with(courseName);
            rabbitAdmin.declareBinding(binding);

            log.info("Course " + courseName + " added successfully!");

            // 创建该队列的消费者，并让其监听队列
            hwConsumer.addConsumer(courseName);

            // 创建作业提交队列
            Queue queueSub = new Queue(courseName+"sub");
            rabbitAdmin.declareQueue(queueSub);
            // 绑定队列到交换机
            Binding bindingSub = BindingBuilder.bind(queueSub).to(directExchange).with(courseName+"sub");
            rabbitAdmin.declareBinding(bindingSub);

            log.info("SubmissionQueue " + courseName + "sub added successfully!");

            // 创建该队列的消费者，并让其监听队列
            submissionConsumer.addConsumer(courseName+"sub");

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
            submissionConsumer.removeConsumer(queueName+"sub");
            return baseVO.success();
        } else {    // 若不为教师角色
            return baseVO.failure().setData("不为教师角色，无法删除课程！");
        }
    }

    /**
     * 学生查看系统中所有课程
     */
    public BaseVO<List<CourseInfo4StudentsVO>> getAllForStudent() {
        // 获取系统中所有课程
        List<TbCourse> courses = tbCourseService.list();
        // 获取所有涉及到的teacherId
        List<Integer> teacherIds = courses.stream()
                .map(TbCourse::getTeacherId)
                .toList();
        // 从用户表中批查得到教师信息
        List<TbUser> teachers = tbUserService.listByIds(teacherIds);

        // teacherId -> teacherName
        Map<Integer, String> teacherMap;    // 存放键值对<教师id, 教师姓名>
        if (CollectionUtil.isEmpty(teacherIds)) {
            teacherMap = Maps.newHashMap();
            log.error("empty teacher ids");
        } else {
            // teacherId -> teacherName
            teacherMap = teachers.stream()
                    .collect(Collectors.toMap(TbUser::getId, TbUser::getUsername));
        }
        // List<TbCourse> -> List<CourseInfo4StudentsVO>
        List<CourseInfo4StudentsVO> courseInfo = courses.stream().map(
                x -> {
                    Integer courseId = x.getId();
                    return CourseInfo4StudentsVO.builder()
                            .id(courseId)
                            .title(x.getTitle())
                            .description(x.getDescription())
                            .teacherId(x.getTeacherId())
                            .teacherName(teacherMap.get(x.getTeacherId()))
                            .enrollment(x.getEnrollment())
                            .build();
                }
        ).toList();

        BaseVO<List<CourseInfo4StudentsVO>> baseVO =
                new BaseVO<List<CourseInfo4StudentsVO>>().success();
        baseVO.setData(courseInfo);
        return baseVO;
    }

    /**
     * 学生选课
     */
    public BaseVO<Object> chooseCourse(Integer courseId) {
        BaseVO<Object> baseVO = new BaseVO<>();
        // 获取当前登录用户
        UserDTO user = UserHolder.get();
        // 验证是否为学生角色
        if(RoleEnum.STUDENT.getCode().equals(user.getRole())) {
            // 学生id
            Integer studentId = user.getId();
            // 查询是否有该学生对该课程的选课记录
            TbStudentCourse record = tbStudentCourseService.recordExists(studentId, courseId);
            // 若已选过该课程
            if(record != null) {
                return baseVO.success().setData("您已选过该课程");
            } else {    // 没选过该课程
                // 创建一条选课记录
                TbStudentCourse newRecord = TbStudentCourse.builder()
                        .studentId(studentId)
                        .courseId(courseId)
                        .build();
                // 将选课记录插入DB
                tbStudentCourseService.save(newRecord);
                // 对所选课程的选课人数+1
                TbCourse course = tbCourseService.getById(courseId);
                course.setEnrollment(course.getEnrollment() + 1);
                tbCourseService.updateById(course);

                return baseVO.success().setData("选课成功！");
            }
        } else {    // 不为学生角色，不能进行选课
            log.error("不为学生角色，无法选课！");
            return baseVO.failure().setData("不为学生角色，无法选课！");
        }
    }

    /**
     * 查找当前登录学生用户的已选课程
     */
    public BaseVO<List<ChosenCourseInfo4StudentsVO>> getChosen() {
        // 获取当前学生用户
        UserDTO user = UserHolder.get();
        // 学生id
        Integer studentId = user.getId();
        // 在tb_student_course表中查询该学生的所有选课记录
        QueryWrapper<TbStudentCourse> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("student_id", studentId);
        List<TbStudentCourse> records = tbStudentCourseService.list(queryWrapper);
        // 获取所有涉及到的courseId
        List<Integer> courseIds = records.stream()
                .map(TbStudentCourse::getCourseId)
                .toList();
        // 从课程表中批查得到相关的课程信息
        List<TbCourse> courses = tbCourseService.listByIds(courseIds);
        // 获取所有涉及到的teacherId
        List<Integer> teacherIds = courses.stream()
                .map(TbCourse::getTeacherId)
                .toList();
        // 从用户表中批查得到教师信息
        List<TbUser> teachers = tbUserService.listByIds(teacherIds);
        // teacherId -> teacherName
        Map<Integer, String> teacherMap;    // 存放键值对<教师id, 教师姓名>
        if (CollectionUtil.isEmpty(teacherIds)) {
            teacherMap = Maps.newHashMap();
            log.error("empty teacher ids");
        } else {
            // teacherId -> teacherName
            teacherMap = teachers.stream()
                    .collect(Collectors.toMap(TbUser::getId, TbUser::getUsername));
        }
        // 获得该学生在course列表中每门课分别有几个未完成作业
        // 1. courseId与该门课程对应的所有作业id（List<Homework>）的映射
        // 创建Map来存储每个课程ID对应的作业列表
        Map<Integer, List<TbHomework>> courseIdToHomeworkMap = courseIds.stream()
                .collect(Collectors.toMap(
                        x -> x,
                        x -> tbHomeworkService.list(new QueryWrapper<TbHomework>()
                                .eq("course_id", x))
                ));
        //2. 遍历courseIdToHomeworkMap Map<Integer, List<Homework>> 并对每个课程计算未完成的作业数量
        // 创建新的Map来存储未完成的作业数量 Map<课程id, 该学生该课程未完成的作业数量>
        Map<Integer, Integer> unfinishedHWMap = new HashMap<>();
        courseIdToHomeworkMap.forEach((courseId, homeworks) -> {
            int unfinishedCount = 0;
            // 该课程的每个作业
            for (TbHomework homework : homeworks) {
                // 在tb_homework_read表中查询作业是否已完成 未完成则得到的count=1，否则为0
                int count = (int)tbHomeworkReadService.count(new QueryWrapper<TbHomeworkRead>()
                        .eq("homework_id", homework.getId())
                        .eq("student_id", studentId)
                        .eq("status", 2));
                if (count > 0) {
                    unfinishedCount += count;
                }
            }
            unfinishedHWMap.put(courseId, unfinishedCount);
        });

        // List<TbCourse> -> List<CourseInfo4StudentsVO>
        List<ChosenCourseInfo4StudentsVO> courseInfo = courses.stream().map(
                x -> {
                    Integer courseId = x.getId();
                    return ChosenCourseInfo4StudentsVO.builder()
                            .id(courseId)
                            .title(x.getTitle())
                            .description(x.getDescription())
                            .teacherId(x.getTeacherId())
                            .teacherName(teacherMap.get(x.getTeacherId()))
                            .enrollment(x.getEnrollment())
                            .toFinish(unfinishedHWMap.get(courseId))
                            .build();
                }
        ).toList();

        BaseVO<List<ChosenCourseInfo4StudentsVO>> baseVO =
                new BaseVO<List<ChosenCourseInfo4StudentsVO>>().success();
        baseVO.setData(courseInfo);
        return baseVO;
    }

}
