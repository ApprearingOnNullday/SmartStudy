package com.michelle.smartstudy.service.base.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.michelle.smartstudy.mapper.TbSubmissionMapper;
import com.michelle.smartstudy.model.entity.TbSubmission;
import com.michelle.smartstudy.service.base.ITbSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 作业提交表 服务实现类
 * </p>
 *
 * @author AppearingOnNullday
 * @since 2024-05-20
 */
@Service
@Slf4j
public class TbSubmissionServiceImpl extends ServiceImpl<TbSubmissionMapper, TbSubmission> implements ITbSubmissionService {
}
