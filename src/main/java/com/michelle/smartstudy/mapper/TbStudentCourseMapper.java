package com.michelle.smartstudy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.michelle.smartstudy.model.entity.TbStudentCourse;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 学生课程表 Mapper 接口
 * </p>
 *
 * @author AppearingOnNullday
 * @since 2024-05-22
 */
@Mapper
public interface TbStudentCourseMapper extends BaseMapper<TbStudentCourse> {
}
