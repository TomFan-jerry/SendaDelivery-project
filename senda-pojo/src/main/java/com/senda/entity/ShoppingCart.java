package com.senda.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * 购物车
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShoppingCart {

    private Long id; // 主键
    private String name; // 商品名称
    private String image; // 图片
    private Long userId; // 主键
    private Long dishId; // 菜品id
    private Long setmealId; // 套餐id
    private String dishFlavor; // 口味
    private Integer number; // 数量
    private BigDecimal amount; // 金额
    private LocalDateTime createTime; // 创建时间
}