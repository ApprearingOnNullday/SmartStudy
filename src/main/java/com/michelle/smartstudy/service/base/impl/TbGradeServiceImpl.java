package com.michelle.smartstudy.service.base.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.michelle.smartstudy.mapper.TbGradeMapper;
import com.michelle.smartstudy.model.entity.TbGrade;
import com.michelle.smartstudy.service.base.ITbGradeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 分数表 服务实现类
 * </p>
 *
 * @author AppearingOnNullday
 * @since 2024-05-20
 */
@Service
@Slf4j
public class TbGradeServiceImpl extends ServiceImpl<TbGradeMapper, TbGrade> implements ITbGradeService {
}
