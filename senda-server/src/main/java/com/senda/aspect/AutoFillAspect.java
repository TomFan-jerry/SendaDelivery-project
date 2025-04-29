package com.senda.aspect;

import com.senda.annotation.AutoFill;
import com.senda.constant.AutoFillConstant;
import com.senda.constant.MessageConstant;
import com.senda.context.AutoFillEntityContext;
import com.senda.context.JwtUserContext;
import com.senda.enumeration.EntityType;
import com.senda.enumeration.OperationType;
import com.senda.exceptions.MissingAutoFillAnnotationException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 自定义切面，实现公共字段自动填充处理逻辑
 */
@Aspect
@Slf4j
@Component
public class AutoFillAspect {

    /**
     * 指定切入点：拦截所有标注了 @AutoFill 注解的方法
     */
    @Pointcut("@annotation(com.senda.annotation.AutoFill)")
    public void autoFillPointcut() {
    }

    /**
     * 使用 @Before 前置通知，在目标方法执行前进行自动填充
     * @param joinPoint
     * @throws Throwable
     */
    @Before("autoFillPointcut()")
    public void autoFill(JoinPoint joinPoint) throws Throwable {
        log.info("开始进行公共字段的自动填充...,{}", joinPoint);

        // 1. 获取目标方法的签名,签名对象
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        // 2. 获取方法上的 @AutoFill 注解
        AutoFill autoFill = method.getAnnotation(AutoFill.class);

        // 3. 从注解中获取数据库操作类型（INSERT、UPDATE）和实体类型（EntityType 枚举）
        if (autoFill == null) {
            throw new MissingAutoFillAnnotationException(MessageConstant.MISSING_AUTOFILL_ANNOTATION);
        }
        OperationType operationType = autoFill.operationType();
        EntityType entityType = autoFill.entityType();

        // 4. 根据 EntityType 枚举，反射创建对应的实体类对象
        Class<?> entityClass = entityType.getEntityClass();
        Object entity = entityClass.getDeclaredConstructor().newInstance();

        // 5. 获取当前时间和当前用户ID，用于公共字段填充
        LocalDateTime now = LocalDateTime.now();
        Long currentUserId = JwtUserContext.getCurrentUserId();

        // 6. 根据数据库操作类型进行公共字段的自动填充
        if (operationType == OperationType.INSERT || operationType == OperationType.UPDATE) {

            // update_time 和 update_user 是 INSERT 和 UPDATE 都需要的
            Method setUpdateTime = entityClass.getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
            Method setUpdateUser = entityClass.getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
            setUpdateTime.invoke(entity, now);
            setUpdateUser.invoke(entity, currentUserId);

            if (operationType == OperationType.INSERT) {

                // insert 还需要额外设置 create_time 和 create_user
                Method setCreateTime = entityClass.getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = entityClass.getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                setCreateTime.invoke(entity, now);
                setCreateUser.invoke(entity, currentUserId);
            }
        }

        // 7. 保存处理结果到 ThreadLocal 中，供后续使用
        AutoFillEntityContext.setCurrentEntity(entity);
    }

    /**
     * 后置清理：在方法执行完毕后清除临时数据，防止内存泄漏。
     * 调用 AutoFillEntityContext.clear() 来清理与自动填充操作相关的所有上下文数据。
     */
    @After("autoFillPointcut()")
    public void clearAutoFillEntityContext() {
        AutoFillEntityContext.clear();
    }
}
