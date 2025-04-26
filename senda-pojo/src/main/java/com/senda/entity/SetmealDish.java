package com.senda.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.math.BigDecimal;

/**
 * 套餐菜品关系
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SetmealDish {

    private Long id; // 主键
    private Long setmealId; // 套餐id
    private Long dishId; // 菜品id
    private String name; // 菜品名称 （冗余字段）
    private BigDecimal price; // 菜品单价（冗余字段）
    private Integer copies; // 菜品份数
}