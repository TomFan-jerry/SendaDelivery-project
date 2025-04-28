package com.senda.utils;

import com.senda.constant.JwtConstant;
import com.senda.context.JwtUserContext;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.Map;

public class JwtUtils {

/*    // 使用硬编码的自定义密钥，确保长度足够满足 HMAC-SHA256 要求（256 位，32 字节）
    private static final String SECRET_KEY = "sendaDeliveryExampleSuperSecretKeyForHS256Algorithm"; // 自定义密钥，至少 256 位
    private static final long EXPIRATION_TIME = 3600000L; // 过期时间，单位：毫秒（此处为 1 小时）*/

    /**
     * 生成 JWT 令牌
     * @param claims JWT 第二部分负载（payload）中存储的内容
     * @return 生成的 JWT 令牌
     */
    public static String generateToken(Map<String, Object> claims) {
        // 使用自定义的密钥并生成符合 HMAC-SHA256 算法要求的密钥
        Key key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

        return Jwts.builder()
                .setClaims(claims)  // 添加自定义 claims
                .setIssuedAt(new Date())  // 设置签发时间
                .setExpiration(new Date(System.currentTimeMillis() + JwtConstant.EXPIRATION_TIME))  // 设置过期时间
                .signWith(key, SignatureAlgorithm.HS256)  // 使用 HS256 算法和密钥进行签名
                .compact();  // 返回生成的 JWT 令牌
    }

    /**
     * 解析 JWT 令牌并设置当前用户ID
     * @param token JWT 令牌
     * @return JWT 负载部分的内容
     * @throws JwtException 如果解析失败或令牌过期
     */
    public static Claims parseToken(String token) throws JwtException {
        Key key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

        // 解析 JWT 令牌并获取用户 ID
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
     * @param token JWT 令牌
     * @return 令牌的主体（subject）
     * @throws JwtException 解析异常
     */
    public static String getSubject(String token) throws JwtException {
        return parseToken(token).getSubject();
    }

    /**
     * 检查 JWT 令牌是否过期
     * @param token JWT 令牌
     * @return 如果过期返回 true，否则返回 false
     * @throws JwtException 解析异常
     */
    public static boolean isTokenExpired(String token) throws JwtException {
        Date expiration = parseToken(token).getExpiration();
        return expiration.before(new Date());  // 返回是否过期
    }
}
