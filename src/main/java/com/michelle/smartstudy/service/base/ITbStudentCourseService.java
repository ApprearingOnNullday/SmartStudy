package com.michelle.smartstudy.service.base;

import com.baomidou.mybatisplus.extension.service.IService;
import com.michelle.smartstudy.model.entity.TbStudentCourse;

/**
 * <p>
 * 学生选课表 服务类
 * </p>
 *
 * @author AppearingOnNullday
 * @since 2024-05-22
 */
public interface ITbStudentCourseService extends IService<TbStudentCourse> {

    // 查询是否存在某学生某课程的选课记录
    TbStudentCourse recordExists(Integer studentId, Integer courseId);

}
