package com.senda.aspect;

import com.senda.annotation.AutoFill;
import com.senda.constant.AutoFillConstant;
import com.senda.context.JwtUserContext;
import com.senda.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 自定义切面，实现公共字段自动填充处理逻辑
 */
@Aspect
@Slf4j
@Component
public class AutoFillAspect {

    /**
     * 切入点
     */
    @Pointcut("@annotation(com.senda.annotation.AutoFill)")
    public void autoFillPointcut() {
    }


    /**
     * 前置通知
     */
    //TODO 根据MybatisPlus优化后的代码实现AOP管理
    @Before("autoFillPointcut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("开始进行公共字段的自动填充...,{}", joinPoint);

        //获取数据库操作类型
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        OperationType operationType = Objects.requireNonNull(methodSignature.getMethod().getAnnotation(AutoFill.class)).value();

        //获取方法参数（实体对象）
        Object entity = joinPoint.getArgs()[0];

        //赋值的数据
        LocalDateTime now = LocalDateTime.now();
        Long currentUserId = JwtUserContext.getCurrentUserId();

        //根据操作类型赋值
        switch (operationType) {
            case INSERT -> {
                try {
                    //获取 entity 对象的特定方法
                    Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                    Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                    Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                    Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                    //将字段值设置到 entity 对象中
                    setCreateTime.invoke(entity, now);
                    setCreateUser.invoke(entity, currentUserId);
                    setUpdateTime.invoke(entity, now);
                    setUpdateUser.invoke(entity, currentUserId);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            case UPDATE -> {
                try {
                    //获取 entity 对象的特定方法
                    Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                    Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                    //将字段值设置到 entity 对象中
                    setUpdateTime.invoke(entity, now);
                    setUpdateUser.invoke(entity, currentUserId);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
