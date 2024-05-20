package com.michelle.smartstudy.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.michelle.smartstudy.model.dto.UserDTO;
import com.michelle.smartstudy.model.vo.BaseVO;
import com.michelle.smartstudy.service.base.ITbUserService;
import com.michelle.smartstudy.utils.JWTUtil;
import com.michelle.smartstudy.utils.UserHolder;
import io.jsonwebtoken.Claims;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Slf4j
@Order(value = Integer.MAX_VALUE >> 1)
public class LoginInterceptor implements HandlerInterceptor {

    private final static String TOKEN = "token";

    private ITbUserService userService;

    public LoginInterceptor(ITbUserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader(TOKEN);
        log.info("token的值：{}",token);
        if (StrUtil.isBlank(token)) {
            // 无token,认证失败
            log.info("token为空，认证失败。request: {}", request);
            return true;  // todo 本该返回 false，此处为便于测试 不做拦截 返回 true
        }

        // 解析token
        Claims claims = JWTUtil.parseJWT(token);

        Integer id = claims.get("id", Integer.class);
        String username = claims.get("username", String.class);
        Integer role = claims.get("role", Integer.class);
        if (id == null || username == null || role == null) {
            log.error("未从token中解析得到需要的信息, token: {}, id: {}, username: {}, role: {}",
                    token, id, username, role);
            writeFailedMsg(response, "token错误。");
            return false;
        }

        // 保存用户对象上下文
        UserDTO loginUser = UserDTO.builder()
                            .id(id)
                            .username(username)
                            .role(role)
                            .build();

        UserHolder.save(loginUser);
        return true;
    }

    // 在响应体中写指定失败msg
    private void writeFailedMsg(HttpServletResponse response, String s) {
        BaseVO<String> baseVO = new BaseVO<String>().failure();
        baseVO.setData(s);
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            outputStream.write(JSONUtil.toJsonStr(baseVO).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 每次请求清理 tl, 防内存泄漏
        UserHolder.remove();
    }
}
