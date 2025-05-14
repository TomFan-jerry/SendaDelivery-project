package com.senda.dto;

import com.senda.entity.DishFlavor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Dish DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DishDTO {

    private Long id; // 主键
    private String name; // 菜品名称
    private Long categoryId; // 菜品分类id
    private BigDecimal price; // 菜品价格
    private String image; // 图片
    private String description; // 描述信息
    private Integer status; // 0 停售 1 起售
    private List<DishFlavor> flavors; // 菜品包含的口味
}