package com.senda.utils;

import com.senda.context.JwtUserContext;
import com.senda.enumeration.TokenType;
import com.senda.properties.JwtProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 生成 JWT 令牌
     *
     * @param claims JWT 第二部分负载（payload）中存储的内容
     * @return 生成的 JWT 令牌
     */
    public String generateToken(Map<String, Object> claims, TokenType tokenType) {
        // 获取配置信息
        String secretKeyString = switch (tokenType) {
            case ADMIN -> jwtProperties.getAdminSecretKey();
            case USER -> jwtProperties.getUserSecretKey();
        };
        Long expirationTime = switch (tokenType) {
            case ADMIN -> jwtProperties.getAdminTtl();
            case USER -> jwtProperties.getUserTtl();
        };

        // 生成符合 HMAC-SHA256 算法要求的密钥
        Key key = Keys.hmacShaKeyFor(secretKeyString.getBytes());

        return Jwts.builder()
                .setClaims(claims)  // 添加自定义 claims
                .setIssuedAt(new Date())  // 设置签发时间
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))  // 设置过期时间
                .signWith(key, SignatureAlgorithm.HS256)  // 使用 HS256 算法和密钥进行签名
                .compact();  // 返回生成的 JWT 令牌
    }

    /**
     * 解析 JWT 令牌并设置当前用户ID
     *
     * @param token JWT 令牌
     * @return JWT 负载部分的内容
     * @throws JwtException 如果解析失败或令牌过期
     */
    public Claims parseToken(String token, TokenType tokenType) throws JwtException {
        // 获取配置信息
        String secretKeyString = switch (tokenType) {
            case ADMIN -> jwtProperties.getAdminSecretKey();
            case USER -> jwtProperties.getUserSecretKey();
        };

        // 解析 JWT 令牌并获取用户 ID
        Key key = Keys.hmacShaKeyFor(secretKeyString.getBytes());

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        // 从 JWT 负载中提取当前用户ID并设置
        Long userId = claims.get("id", Long.class); // 假设 JWT 负载中有 "id" 字段
        JwtUserContext.setCurrentUserId(userId); // 设置当前用户ID

        return claims;
    }

    /**
     * 获取 JWT 令牌的主体（subject）
     *
     * @param token JWT 令牌
     * @return 令牌的主体（subject）
     * @throws JwtException 解析异常
     */
    public String getSubject(String token, TokenType tokenType) throws JwtException {
        return parseToken(token, tokenType).getSubject();
    }

    /**
     * 检查 JWT 令牌是否过期
     *
     * @param token JWT 令牌
     * @return 如果过期返回 true，否则返回 false
     * @throws JwtException 解析异常
     */
    public boolean isTokenExpired(String token, TokenType tokenType) throws JwtException {
        Date expiration = parseToken(token, tokenType).getExpiration();
        return expiration.before(new Date());  // 返回是否过期
    }
}
