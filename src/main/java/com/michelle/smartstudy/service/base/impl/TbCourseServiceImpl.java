package com.michelle.smartstudy.service.base.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.michelle.smartstudy.mapper.TbCourseMapper;
import com.michelle.smartstudy.model.entity.TbCourse;
import com.michelle.smartstudy.service.base.ITbCourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程表 服务实现类
 * </p>
 *
 * @author AppearingOnNullday
 * @since 2024-05-20
 */
@Service
@Slf4j
public class TbCourseServiceImpl extends ServiceImpl<TbCourseMapper, TbCourse> implements ITbCourseService {
}
