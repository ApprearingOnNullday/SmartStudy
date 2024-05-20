package com.michelle.smartstudy.api.http;

import com.michelle.smartstudy.model.vo.BaseVO;
import com.michelle.smartstudy.model.vo.LoginUserVO;
import com.michelle.smartstudy.service.business.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author AppearingOnNullday
 * @since 2024-05-20
 */
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return
     */
    @PostMapping("/login")
    public BaseVO<LoginUserVO> login(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpServletResponse response){
        log.info("登录:username:{},password:{}",username,password);
        return userService.login(username, password, response);
    }

}
