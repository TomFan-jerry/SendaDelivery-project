package com.senda.context;

/**
 * 通用上下文管理类
 */
public class BaseContext {

    // 清除当前线程的所有上下文信息
    public static void clearAll() {
        JwtUserContext.clear();  // 调用 JwtUserContext 清除员工上下文信息
        // 如果以后有其他上下文类（例如用户上下文），也可以在此调用清除
    }
}