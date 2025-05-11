package com.senda.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Setmeal DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SetmealDTO {

    private Long id; // 主键
    private Long categoryId; // 菜品分类id
    private String name; // 套餐名称
    private BigDecimal price; // 套餐价格
    private Integer status; // 售卖状态 0:停售 1:起售
    private String description; // 描述信息
    private String image; // 图片
}