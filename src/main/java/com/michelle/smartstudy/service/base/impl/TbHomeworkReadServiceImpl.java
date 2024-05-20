package com.michelle.smartstudy.service.base.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.michelle.smartstudy.mapper.TbHomeworkReadMapper;
import com.michelle.smartstudy.model.entity.TbHomeworkRead;
import com.michelle.smartstudy.service.base.ITbHomeworkReadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 作业已读表 服务实现类
 * </p>
 *
 * @author AppearingOnNullday
 * @since 2024-05-20
 */
@Service
@Slf4j
public class TbHomeworkReadServiceImpl extends ServiceImpl<TbHomeworkReadMapper, TbHomeworkRead> implements ITbHomeworkReadService {
}
