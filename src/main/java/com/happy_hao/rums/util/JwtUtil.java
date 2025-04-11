package com.happy_hao.rums.util;

import com.happy_hao.rums.config.JwtConfig;
import com.happy_hao.rums.exception.ServiceException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    private final long jwtExpiration;
    private final SecretKey key;

    // 使用构造函数注入
    @Autowired
    public JwtUtil(JwtConfig jwtConfig) {
        this.jwtExpiration = jwtConfig.getExpirationTime();
        this.key = Keys.hmacShaKeyFor(jwtConfig.getSecretKey().getBytes());
    }

    public String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .claims().empty().add(claims).and()
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration)) // 使用静态字段
                .signWith(key) // 使用静态密钥对象
                .compact();
    }

    public Claims extractClaims(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .verifyWith(key) // 使用静态密钥对象
                    .build()
                    .parseSignedClaims(token);
            // 获取 Claims
            Claims claims = claimsJws.getPayload();

            // 验证过期时间
            Instant expiration = Instant.ofEpochSecond(claims.getExpiration().getTime() / 1000L);
            if (Instant.now().isAfter(expiration)) {
                throw new ServiceException("登录已过期");
            }

            return claims;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }
}