package com.michelle.smartstudy.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Configuration
@ConfigurationProperties("jwt")
public class JWTUtil {

    // 原始字符串 -> Base64编码
    public static String Base64Encoding() {
        String originalString = "This is SA Group 12." +
                "Have to build a key long enough for the algorithm to run";
        String encodedString = Base64.getEncoder().encodeToString(originalString.getBytes());
        return encodedString;   // 编码后：VGhpcyBpcyBTQSBHcm91cCAxMi5IYXZlIHRvIGJ1aWxkIGEga2V5IGxvbmcgZW5vdWdoIGZvciB0aGUgYWxnb3JpdGhtIHRvIHJ1bg==
    }

    private static String secretKeyStr = "VGhpcyBpcyBTQSBHcm91cCAxMi5IYXZlIHRvIGJ1aWxkIGEga2V5IGxvbmcgZW5vdWdoIGZvciB0aGUgYWxnb3JpdGhtIHRvIHJ1bg==";

    private static int ttl = 99;  // 单位： 天

    private static SecretKey secretKey;

    /*
        1.接受Base64编码的密钥字符串
        2.将其解码为原始的字节数组
        3.使用该字节数组创建一个用于HMAC-SHA256算法的SecretKey对象
    */
    public static SecretKey getSecretKey(String keyStr) {
        byte[] decodedKey = Base64.getDecoder().decode(keyStr);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");
    }

    // 生成JWT令牌
    public static String generateJWT(Map<String, Object> claims) {
        if (secretKey == null) {
            secretKey = getSecretKey(secretKeyStr);
        }

        long msTtl =  24L * 3600 * 1000 * ttl;

        return  Jwts.builder()
                .claims(claims)     // payload
                .expiration(new Date(System.currentTimeMillis() + msTtl))   // 过期时间
                .signWith(secretKey)    // signature
                .compact();
    }

    // 解析JWT令牌
    /**
     * 解析JWT令牌
     * @param jwt 令牌（String形式）
     * @return Claims JWT令牌的payload
     */
    public static Claims parseJWT(String jwt) {
        if (secretKey == null) {
            secretKey = getSecretKey(secretKeyStr);
        }

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }

}
