package com.senda.annotation;

import com.senda.enumeration.EntityType;
import com.senda.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识某个方法需要进行功能字段自动填充
 */
@Target(ElementType.METHOD)  // 注解作用于方法上
@Retention(RetentionPolicy.RUNTIME)  // 运行时保留，用于反射读取
public @interface AutoFill {

    /**
     * 数据库操作类型
     * @return
     */
    OperationType operationType();

    /**
     * 实体类型，用于反射创建实体类对象
     * @return
     */
    EntityType entityType();
}
