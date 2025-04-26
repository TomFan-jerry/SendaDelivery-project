package com.senda.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * 菜品口味关系表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DishFlavor {

    private Long id; // 主键
    private Long dishId; // 菜品
    private String name; // 口味名称
    private String value; // 口味数据list
}