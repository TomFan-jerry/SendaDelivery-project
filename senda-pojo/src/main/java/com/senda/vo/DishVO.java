package com.senda.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Dish VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DishVO {

    private Long id; // 主键
    private String name; // 菜品名称
    private Long categoryId; // 菜品分类id
    private BigDecimal price; // 菜品价格
    private String image; // 图片
    private String description; // 描述信息
    private Integer status; // 0 停售 1 起售
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间
    private Long createUser; // 创建人
    private Long updateUser; // 修改人
    private String categoryName; // 菜品分类名称
}