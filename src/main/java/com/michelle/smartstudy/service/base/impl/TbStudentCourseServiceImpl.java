package com.michelle.smartstudy.service.base.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.michelle.smartstudy.mapper.TbStudentCourseMapper;
import com.michelle.smartstudy.model.entity.TbStudentCourse;
import com.michelle.smartstudy.service.base.ITbStudentCourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 学生选课表 服务实现类
 * </p>
 *
 * @author AppearingOnNullday
 * @since 2024-05-22
 */
@Service
@Slf4j
public class TbStudentCourseServiceImpl extends ServiceImpl<TbStudentCourseMapper, TbStudentCourse> implements ITbStudentCourseService {

    // 查询是否存在某学生某课程的选课记录
    @Override
    public TbStudentCourse recordExists(Integer studentId, Integer courseId) {
        TbStudentCourse record = this.getOne(new QueryWrapper<TbStudentCourse>()
                .eq("student_id", studentId)
                .eq("course_id", courseId));
        return record;
    }

}
