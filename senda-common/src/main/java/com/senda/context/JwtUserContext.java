package com.senda.context;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtUserContext extends BaseContext{

    // 使用 ThreadLocal 存储当前线程的用户ID（JWT 解析后）
    private static final ThreadLocal<Long> currentUserContext = new ThreadLocal<>();

    // 设置当前用户ID（从 JWT 令牌中解析出来）
    public static void setCurrentUserId(Long userId) {
        currentUserContext.set(userId);
    }

    // 获取当前用户ID
    public static Long getCurrentUserId() {
        return currentUserContext.get();
    }

    // 清除当前线程的用户信息
    public static void clear() {
        log.info("清除线程信息...:{}", currentUserContext);
        currentUserContext.remove();
    }
}
