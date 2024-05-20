package com.michelle.smartstudy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.michelle.smartstudy.model.entity.TbUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author AppearingOnNullday
 * @since 2024-05-20
 */
@Mapper
public interface TbUserMapper extends BaseMapper<TbUser> {
}
