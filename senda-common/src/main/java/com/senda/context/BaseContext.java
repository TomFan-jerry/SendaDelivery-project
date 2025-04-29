package com.senda.context;

import lombok.extern.slf4j.Slf4j;

/**
 * 通用上下文管理类
 */
@Slf4j
public class BaseContext {

    // 清除当前线程的所有上下文信息
    public static void clearAll() {
        log.info("清除所有线程信息...");
        JwtUserContext.clear();
        AutoFillEntityContext.clear();// 调用 JwtUserContext 清除员工上下文信息
        // 如果以后有其他上下文类（例如用户上下文），也可以在此调用清除
    }
}