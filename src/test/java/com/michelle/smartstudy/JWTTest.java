package com.michelle.smartstudy;

import com.google.common.collect.Maps;
import com.michelle.smartstudy.model.enums.RoleEnum;
import com.michelle.smartstudy.utils.JWTUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
public class JWTTest {

    // 测试Base64编码
    @Test
    public void EncodeKey() {
        System.out.println(JWTUtil.Base64Encoding());
    }

    // 获得一个token
    @Test
    public void getToken() {
        Map<String, Object> claims = Maps.newHashMap();
        claims.put("id", 1);
        claims.put("username", "AppearingOnNullday");
        claims.put("role", RoleEnum.STUDENT.getCode());
        // 生成一个JWT令牌
        String jwt = JWTUtil.generateJWT(claims);
        System.out.println(jwt);
    }

}
