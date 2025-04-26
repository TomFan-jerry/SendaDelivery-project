package com.senda.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.math.BigDecimal;

/**
 * 订单明细表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetail {

    private Long id; // 主键
    private String name; // 名字
    private String image; // 图片
    private Long orderId; // 订单id
    private Long dishId; // 菜品id
    private Long setmealId; // 套餐id
    private String dishFlavor; // 口味
    private Integer number; // 数量
    private BigDecimal amount; // 金额
}