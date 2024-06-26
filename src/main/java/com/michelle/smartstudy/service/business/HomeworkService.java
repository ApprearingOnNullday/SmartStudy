package com.michelle.smartstudy.service.business;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Maps;
import com.michelle.smartstudy.model.dto.HomeworkDTO;
import com.michelle.smartstudy.model.dto.SubmissionDTO;
import com.michelle.smartstudy.model.dto.UserDTO;
import com.michelle.smartstudy.model.entity.*;
import com.michelle.smartstudy.model.enums.CorrectingStatusEnum;
import com.michelle.smartstudy.model.enums.ReadStatusEnum;
import com.michelle.smartstudy.model.query.HWAssignQuery;
import com.michelle.smartstudy.model.query.HWSubmitQuery;
import com.michelle.smartstudy.model.vo.*;
import com.michelle.smartstudy.mq.producer.HomeworkProducer;
import com.michelle.smartstudy.mq.producer.SubmissionProducer;
import com.michelle.smartstudy.service.base.*;
import com.michelle.smartstudy.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Autowired
    private ITbSubmissionService tbSubmissionService;

    @Autowired
    private ITbUserService tbUserService;

    @Autowired
    private ITbHomeworkReadService tbHomeworkReadService;

    @Autowired
    private ITbGradeService tbGradeService;

    // 教师布置作业
    public BaseVO<Object> assign(Integer id, HWAssignQuery hwAssignQuery) {
        // 将前端传的时间数据 由String -> LocalDateTime
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
//        LocalDateTime start = LocalDateTime.parse(hwAssignQuery.getStart(), formatter);
//        LocalDateTime end = LocalDateTime.parse(hwAssignQuery.getEnd(), formatter);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
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
        log.info("send msg : {}  To queue: {}", submissionDTO, courseName+"sub");

        /*
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
        */

        return new BaseVO<>().success().setData("已成功提交作业！");
    }

    // 教师查看自己某门课程布置的所有作业
    public BaseVO<List<HWInfo4TeachersVO>> teacherGetAll(Integer courseId) {
        // 课程总人数（即应收到作业的份数）
        TbCourse course = tbCourseService.getById(courseId);
        Integer total = course.getEnrollment();
        // 查询tb_homework表中所有该课程的作业信息
        LambdaQueryWrapper<TbHomework> query = new LambdaQueryWrapper<TbHomework>()
                .eq(TbHomework::getCourseId, courseId);
        // 创建时间倒序
        query.orderByDesc(TbHomework::getCtime);
        // 获取该课程所有Homework Entity的List
        List<TbHomework> homeworks = tbHomeworkService.list(query);
        // 遍历每个作业，获得每个作业的已完成人数
        Map<Integer, Integer> submittedMap = new HashMap<>();   // map<作业id, 已提交该作业的人数>
        for(TbHomework homework: homeworks) {
            Integer homeworkId = homework.getId();
            // 在tb_submission表中查询已完成该作业的份数
            int count = (int)tbSubmissionService.count(new QueryWrapper<TbSubmission>()
                    .eq("homework_id", homeworkId));
            submittedMap.put(homeworkId, count);
        }
        // List<TbHomework> -> List<HWInfo4TeachersVO>
        List<HWInfo4TeachersVO> infos = homeworks.stream().map(
                x -> {
                    // 获取作业id
                    Integer homeworkId = x.getId();
                    Integer submitted = submittedMap.get(homeworkId);   // 已提交份数
                    return HWInfo4TeachersVO.builder()
                            .id(homeworkId)
                            .title(x.getTitle())
                            .description(x.getDescription())
                            .start(x.getStart())
                            .end(x.getEnd())
                            .submitted(submitted)
                            .total(total)
                            .build();
                }
        ).toList();

        BaseVO<List<HWInfo4TeachersVO>> baseVO =
                new BaseVO<List<HWInfo4TeachersVO>>().success();
        baseVO.setData(infos);
        return baseVO;
    }

    // 教师查看自己某门课程布置的某个作业的所有提交记录(按已提交状态&未提交状态)
    public BaseVO<List<SubmittedHWInfo4TeachersVO>> teacherGetSubmit(Integer homeworkId, Integer status) {
        // 根据作业id在提交表tb_submission中查询所有条目，并按submit_time的正序排序
        LambdaQueryWrapper<TbSubmission> query = new LambdaQueryWrapper<>();
        query.eq(TbSubmission::getHomeworkId, homeworkId)
                .eq(TbSubmission::getStatus, status)
                .orderByAsc(TbSubmission::getSubmitTime);   // 先显示提交的早的
        List<TbSubmission> submissions = tbSubmissionService.list(query);
        // studentId -> studentName Map<学生id, 学生姓名>
        Map<Integer, String> studentMap;
        // 1.所有提交记录中涉及到的学生id
        List<Integer> studentIds = submissions.stream()
                .map(TbSubmission::getStudentId)
                .toList();
        // 2.根据得到的学生id到tb_user表中查对应的userName
        if (CollectionUtil.isEmpty(studentIds)) {
            studentMap = Maps.newHashMap();
            log.error("empty student ids");
        } else {
            List<TbUser> students = tbUserService.listByIds(studentIds);
            studentMap = students.stream()
                    .collect(Collectors.toMap(TbUser::getId, TbUser::getUsername));
        }
        // 得到所有相关的submissionId
        List<Integer> submissionIds = submissions.stream()
                .map(TbSubmission::getId)
                .toList();
        Map<Integer, TbGrade> gradeMap = new HashMap<>();
        if (CollectionUtil.isEmpty(submissionIds)) {
            log.error("empty submission ids");
        } else {
            // submissionId -> TbGrade Entity
            for(Integer submitId: submissionIds) {
                QueryWrapper<TbGrade> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("submission_id", submitId);
                TbGrade tbGrade = tbGradeService.getOne(queryWrapper);
                gradeMap.put(submitId, tbGrade);
            }
        }
        // 转换为VO（List<TbSubmission> -> List<SubmittedHWInfo4TeachersVO>）
        List<SubmittedHWInfo4TeachersVO> infos = submissions.stream().map(
                x -> {
                    Integer studentId = x.getStudentId();   // 学生id
                    Integer submissionId = x.getId();   // 提交id
                    TbGrade tbGrade = gradeMap.get(submissionId); // submissionId对应的TbGrade Entity
                    return SubmittedHWInfo4TeachersVO.builder()
                            .submitId(x.getId())
                            .studentId(studentId)
                            .studentName(studentMap.get(studentId))
                            .submitTime(x.getSubmitTime())
                            .content(x.getContent())
                            .status(status)
                            .statusDesc(CorrectingStatusEnum.getDescByCode(status))
                            .score(tbGrade != null ? tbGrade.getScore() : BigDecimal.valueOf(0))
                            .comment(tbGrade != null ? tbGrade.getComment() : StrUtil.EMPTY)
                            .build();
                }
        ).toList();

        BaseVO<List<SubmittedHWInfo4TeachersVO>> baseVO =
                new BaseVO<List<SubmittedHWInfo4TeachersVO>>().success();
        baseVO.setData(infos);
        return baseVO;
    }

    // 学生查看某门课已布置的作业
    public BaseVO<List<HWInfo4StudentsVO>> studentGetAll(Integer courseId) {
        // 获得当前登录的学生用户
        UserDTO user = UserHolder.get();
        // 学生id
        Integer studentId = user.getId();
        // 查询tb_homework表中所有该课程的作业信息
        LambdaQueryWrapper<TbHomework> query = new LambdaQueryWrapper<TbHomework>()
                .eq(TbHomework::getCourseId, courseId);
        // 创建时间倒序
        query.orderByDesc(TbHomework::getCtime);
        // 获取该课程所有Homework Entity的List
        List<TbHomework> homeworks = tbHomeworkService.list(query);
        // 相关的homeworkId的list
        List<Integer> homeworkIds = homeworks.stream()
                .map(TbHomework::getId)
                .toList();
        // 1. 根据作业id与学生id，到tb_homework_read表中查完成状态 Map<Integer, Integer> <作业id，该学生的完成状态>
        // 2. 建立Map<作业id，提交记录id> （若作业未完成则对应的提交记录id=0）在submission表中没有对应记录说明未完成
        Map<Integer, Integer> finishStatusMap = new HashMap<>();
        Map<Integer, Integer> submitMap = new HashMap<>();
        for(Integer homeworkId: homeworkIds) {
            // 在tb_homework_read表中获取对应条目
            QueryWrapper<TbHomeworkRead> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("homework_id", homeworkId)
                    .eq("student_id", studentId);
            TbHomeworkRead record = tbHomeworkReadService.getOne(queryWrapper);
            // 获得完成状态
            Integer status = record.getStatus();
            // put到map中
            finishStatusMap.put(homeworkId, status);

            // 在tb_submission表中获取对应条目
            QueryWrapper<TbSubmission> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("homework_id", homeworkId)
                    .eq("student_id", studentId);
            TbSubmission res = tbSubmissionService.getOne(queryWrapper1);
            // 若res为空说明没有提交，将submission_id设置为0
            if(res == null) {
                submitMap.put(homeworkId, 0);
            } else {
                submitMap.put(homeworkId, res.getId());
            }
        }
        // 构建VO （List<TbHomework> -> List<HWInfo4StudentsVO>）
        List<HWInfo4StudentsVO> infos = homeworks.stream().map(
                x -> {
                    Integer homeworkId = x.getId(); // 作业id
                    Integer finishStatus = finishStatusMap.get(homeworkId);
                    return HWInfo4StudentsVO.builder()
                            .homeworkId(homeworkId)
                            .title(x.getTitle())
                            .description(x.getDescription())
                            .start(x.getStart())
                            .end(x.getEnd())
                            .status(finishStatus)
                            .statusDesc(ReadStatusEnum.getDescByCode(finishStatus))
                            .submitId(submitMap.get(homeworkId))
                            .build();
                }
        ).toList();

        BaseVO<List<HWInfo4StudentsVO>> baseVO =
                new BaseVO<List<HWInfo4StudentsVO>>().success();
        baseVO.setData(infos);
        return baseVO;
    }

    // 学生查看自己某个已完成作业的提交记录
    public BaseVO<SubmissionInfo4StudentsVO> studentViewSubmit(Integer submitId) {
        // 查询该提交记录对应的TbSubmission Entity
        TbSubmission submission = tbSubmissionService.getById(submitId);
        // 在tb_grade表中获取对应条目
        QueryWrapper<TbGrade> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("submission_id", submitId);
        TbGrade gradeInfo = tbGradeService.getOne(queryWrapper);

        SubmissionInfo4StudentsVO info = SubmissionInfo4StudentsVO.builder()
                .submitTime(submission.getSubmitTime())
                .content(submission.getContent())
                .status(submission.getStatus())
                .statusDesc(CorrectingStatusEnum.getDescByCode(submission.getStatus()))
                .score(gradeInfo != null ? gradeInfo.getScore() : BigDecimal.valueOf(0))
                .comment(gradeInfo != null ? gradeInfo.getComment() : StrUtil.EMPTY)
                .build();

        return new BaseVO<SubmissionInfo4StudentsVO>().success().setData(info);
    }

    // 查看某项作业的详情信息
    public BaseVO<Object> getDetail(Integer homeworkId) {
        TbHomework tbHomework = tbHomeworkService.getById(homeworkId);
        return new BaseVO<>().success().setData(tbHomework);
    }
}
