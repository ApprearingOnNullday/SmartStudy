package com.michelle.smartstudy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.michelle.smartstudy.model.entity.TbCourse;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 课程表 Mapper 接口
 * </p>
 *
 * @author AppearingOnNullday
 * @since 2024-05-20
 */
@Mapper
public interface TbCourseMapper extends BaseMapper<TbCourse> {
}
