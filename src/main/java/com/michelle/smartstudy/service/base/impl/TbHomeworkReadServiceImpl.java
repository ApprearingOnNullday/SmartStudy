package com.michelle.smartstudy.service.base.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.michelle.smartstudy.mapper.TbHomeworkMapper;
import com.michelle.smartstudy.model.entity.TbHomework;
import com.michelle.smartstudy.service.base.ITbHomeworkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 作业表 服务实现类
 * </p>
 *
 * @author AppearingOnNullday
 * @since 2024-05-20
 */
@Service
@Slf4j
public class TbHomeworkServiceImpl extends ServiceImpl<TbHomeworkMapper, TbHomework> implements ITbHomeworkService {
}
