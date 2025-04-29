package com.senda.context;

import lombok.extern.slf4j.Slf4j;

/**
 * 用于在一次请求中临时保存自动填充后的实体对象
 * 可供 Controller 层获取使用，避免反复处理或参数污染
 */
@Slf4j
public class AutoFillEntityContext extends BaseContext{

    // 使用 ThreadLocal 临时存储填充后的实体对象（线程安全，适用于单请求线程）
    private static final ThreadLocal<Object> ENTITY_HOLDER = new ThreadLocal<>();

    /**
     * 设置填充后的实体对象
     * @param entity 填充后的实体对象（如 Employee）
     */
    public static void setCurrentEntity(Object entity) {
        ENTITY_HOLDER.set(entity);
    }

    /**
     * 获取当前线程中存储的实体对象
     * @return 实体对象（例如 Employee）
     */
    public static Object getCurrentEntity() {
        return ENTITY_HOLDER.get();
    }

    /**
     * 清除当前线程中的实体对象，防止内存泄漏
     */
    public static void clear() {
        log.info("清除线程中的实体对象信息: {}", ENTITY_HOLDER.get());
        ENTITY_HOLDER.remove();
    }
}
