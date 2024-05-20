package com.michelle.smartstudy.config;

import com.michelle.smartstudy.interceptor.LoginInterceptor;
import com.michelle.smartstudy.service.base.ITbUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class MvcConfig implements WebMvcConfigurer {


    @Autowired
    private ITbUserService userService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor(userService))
                .excludePathPatterns(
                        "/user/login"
//                        "/user/register"
                );  // todo 排除登陆注册接口
    }
}
