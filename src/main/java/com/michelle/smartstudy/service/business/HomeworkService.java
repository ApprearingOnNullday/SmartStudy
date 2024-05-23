package com.michelle.smartstudy.service.business;

import com.michelle.smartstudy.model.dto.HomeworkDTO;
import com.michelle.smartstudy.model.entity.TbCourse;
import com.michelle.smartstudy.model.query.HWAssignQuery;
import com.michelle.smartstudy.model.vo.BaseVO;
import com.michelle.smartstudy.mq.producer.HomeworkProducer;
import com.michelle.smartstudy.service.base.ITbCourseService;
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
    private ITbCourseService tbCourseService;

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
}
