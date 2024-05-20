package com.michelle.smartstudy.service.base.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.michelle.smartstudy.mapper.TbUserMapper;
import com.michelle.smartstudy.model.entity.TbUser;
import com.michelle.smartstudy.service.base.ITbUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author AppearingOnNullday
 * @since 2024-05-20
 */
@Service
@Slf4j
public class TbUserServiceImpl extends ServiceImpl<TbUserMapper, TbUser> implements ITbUserService {
}
