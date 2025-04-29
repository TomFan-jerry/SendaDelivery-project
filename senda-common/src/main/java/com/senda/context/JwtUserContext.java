package com.senda.context;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtUserContext extends BaseContext{

    // 使用 ThreadLocal 存储当前线程的用户ID（JWT 解析后）
    private static final ThreadLocal<Long> CURRENT_USER_CONTEXT = new ThreadLocal<>();

    // 设置当前用户ID（从 JWT 令牌中解析出来）
    public static void setCurrentUserId(Long userId) {
        CURRENT_USER_CONTEXT.set(userId);
    }

    // 获取当前用户ID
    public static Long getCurrentUserId() {
        return CURRENT_USER_CONTEXT.get();
    }

    // 清除当前线程的用户信息
    public static void clear() {
        log.info("清除线程中的当前用户id信息:{}", CURRENT_USER_CONTEXT.get());
        CURRENT_USER_CONTEXT.remove();
    }
}
