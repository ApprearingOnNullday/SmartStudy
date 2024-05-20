package com.michelle.smartstudy.service.business;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.michelle.smartstudy.model.entity.TbUser;
import com.michelle.smartstudy.model.vo.BaseVO;
import com.michelle.smartstudy.model.vo.LoginUserVO;
import com.michelle.smartstudy.service.base.ITbUserService;
import com.michelle.smartstudy.utils.JWTUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserService {

    @Autowired
    private ITbUserService tbUserService;

    // 用户登录
    public BaseVO<LoginUserVO> login(String username, String password, HttpServletResponse response) {
        BaseVO<LoginUserVO> baseVO = new BaseVO<>();
        // 检查登录参数
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            log.warn("登录参数异常， username:{}, password:{} ", username, password);
            return baseVO.failure();
        }
        // 根据登录信息，到DB查询用户
        TbUser tbUser = tbUserService.getOne(new QueryWrapper<TbUser>()
                .eq("username", username)
                .eq("password", password));
        // 用户不存在，返回登录失败信息
        if(tbUser == null) {
            log.error("登录用户不存在！");
            return baseVO.failure();
        } else {    // 根据用户信息生成token并返回给前端，登录成功
            Map<String, Object> claims = new HashMap<>();
            // 向payload中加入用户id、用户名、角色信息
            claims.put("id", tbUser.getId());
            claims.put("username", tbUser.getUsername());
            claims.put("role", tbUser.getRole());
            // 生成JWT token
            String token = JWTUtil.generateJWT(claims);
            // 在响应头返回token
            response.setHeader("token", token);
            // 封装LoginUserVO
            LoginUserVO user = LoginUserVO.builder()
                                    .id(tbUser.getId())
                                    .username(tbUser.getUsername())
                                    .role(tbUser.getRole())
                                    .token(token)
                                    .build();
            return baseVO.success().setData(user);
        }
    }
}
