package com.senda.enumeration;

import com.senda.entity.Category;
import com.senda.entity.Employee;
import lombok.Getter;

@Getter
public enum EntityType {

    EMPLOYEE(Employee.class),
    CATEGORY(Category.class);

    // 每个枚举常量绑定一个实体类的 Class 类型，用于后续反射实例化对象
    private final Class<?> entityClass;

    // 枚举的构造方法，接收一个 Class<?> 参数，并赋值给 entityClass 字段
    EntityType(Class<?> entityClass) {
        this.entityClass = entityClass;
    }
}
