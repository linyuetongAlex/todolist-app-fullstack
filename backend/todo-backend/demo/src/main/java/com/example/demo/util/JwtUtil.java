package com.example.demo.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    // 密钥，实际项目中应该放到配置文件里，不要硬编码在代码中（我们先跑通功能，后面再优化）
    private final SecretKey key = Keys.hmacShaKeyFor(
            "this-is-a-very-long-secret-key-for-jwt-signing-1234567890".getBytes()
    );

    private final long EXPIRATION_MS = 7 * 24 * 60 * 60 * 1000L; // 7天有效期

    // 生成token：把userId放进Payload
    public String generateToken(String userId) {
        return Jwts.builder()
                .subject(userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(key)
                .compact();
    }

    // 解析token，拿到里面存的userId；如果token非法或过期，这里会自动抛出异常
    public String parseUserId(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }
}