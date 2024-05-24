package com.michelle.smartstudy.service.business;

import com.michelle.smartstudy.model.dto.HomeworkDTO;
import com.michelle.smartstudy.model.dto.SubmissionDTO;
import com.michelle.smartstudy.model.dto.UserDTO;
import com.michelle.smartstudy.model.entity.TbCourse;
import com.michelle.smartstudy.model.entity.TbHomework;
import com.michelle.smartstudy.model.query.HWAssignQuery;
import com.michelle.smartstudy.model.query.HWSubmitQuery;
import com.michelle.smartstudy.model.vo.BaseVO;
import com.michelle.smartstudy.mq.producer.HomeworkProducer;
import com.michelle.smartstudy.mq.producer.SubmissionProducer;
import com.michelle.smartstudy.service.base.ITbCourseService;
import com.michelle.smartstudy.service.base.ITbHomeworkService;
import com.michelle.smartstudy.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class HomeworkService {

    @Autowired
    private HomeworkProducer homeworkProducer;

    @Autowired
    private SubmissionProducer submissionProducer;

    @Autowired
    private ITbCourseService tbCourseService;

    @Autowired
    private ITbHomeworkService tbHomeworkService;

    // 教师布置作业
    public BaseVO<Object> assign(Integer id, HWAssignQuery hwAssignQuery) {
        // 将前端传的时间数据 由String -> LocalDateTime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime start = LocalDateTime.parse(hwAssignQuery.getStart(), formatter);
        LocalDateTime end = LocalDateTime.parse(hwAssignQuery.getEnd(), formatter);
        // 根据课程id获得课程名（即队列名）
        TbCourse course = tbCourseService.getById(id);
        String courseName = course.getTitle();
        // 封装HomeworkDTO
        HomeworkDTO homeworkDTO = HomeworkDTO.builder()
                .title(hwAssignQuery.getTitle())
                .courseId(id)
                .description(hwAssignQuery.getDescription())
                .start(start)
                .end(end)
                .build();
        // 向该课程对应的消息队列中发送一条作业message
        homeworkProducer.sendMsg(courseName, homeworkDTO);
        log.info("send msg : {}  To queue: {}", homeworkDTO, courseName);

        return new BaseVO<>().success().setData("已成功布置作业！");
    }

    // 学生提交作业
    public BaseVO<Object> submit(HWSubmitQuery hwSubmitQuery) {
        // 获取当前登录学生用户
        UserDTO user = UserHolder.get();
        // 封装SubmissionDTO
        SubmissionDTO submissionDTO = SubmissionDTO.builder()
                .studentId(user.getId())
                .homeworkId(hwSubmitQuery.getHomeworkId())
                .submitTime(LocalDateTime.now())    // 将提交时间设置为当前时间
                .content(hwSubmitQuery.getContent())
                .build();
        // 获取作业对应的课程id
        TbHomework tbHomework = tbHomeworkService.getById(hwSubmitQuery.getHomeworkId());
        Integer courseId = tbHomework.getCourseId();
        // 根据课程id获得课程名（即队列名）
        TbCourse course = tbCourseService.getById(courseId);
        String courseName = course.getTitle();
        // 队列名称
        String queueName = courseName + "sub";

        // 向该课程对应的消息队列中发送一条作业message
        submissionProducer.sendMsg(queueName, submissionDTO);
        log.info("send msg : {}  To queue: {}", submissionDTO, courseName);
        return new BaseVO<>().success().setData("已成功提交作业！");
    }
}
