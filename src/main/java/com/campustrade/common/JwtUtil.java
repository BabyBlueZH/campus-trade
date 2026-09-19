package com.campustrade.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT 工具类：生成 / 解析 Token
 * <p>
 * 密钥和过期时间都从 application.yaml 读取，不硬编码在代码里。
 */
@Component
public class JwtUtil {

    /** 签名密钥（HS256 要求至少 32 字节） */
    @Value("${jwt.secret}")
    private String secret;

    /** 过期时间（毫秒） */
    @Value("${jwt.expiration}")
    private long expiration;

    /** 把字符串密钥转成 jjwt 需要的 SecretKey 对象 */
    private SecretKey key() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成 Token
     * <p>
     * payload 里只放 userId 和 username，绝不放密码等敏感信息
     * （payload 只是 base64 编码，任何人都能解开看）
     */
    public String generate(Long userId, String username) {
        return Jwts.builder()
                .claim("userId", userId)
                .claim("username", username)
                .issuedAt(new Date())                                      // 签发时间
                .expiration(new Date(System.currentTimeMillis() + expiration)) // 过期时间
                .signWith(key())                                           // 用密钥签名
                .compact();                                                // 拼成 header.payload.signature
    }

    /**
     * 解析 Token，返回 payload
     * <p>
     * token 被篡改、签名不对、已过期 —— 都会抛异常（由调用方处理）
     */
    public Claims parse(String token) {
        return Jwts.parser()
                .verifyWith(key())          // 用同一个密钥验签
                .build()
                .parseSignedClaims(token)   // 解析并校验签名
                .getPayload();
    }
}
