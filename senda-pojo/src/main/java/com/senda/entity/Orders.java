package com.senda.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * 订单表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Orders {

    private Long id; // 主键
    private String number; // 订单号
    private Integer status; // 订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消 7退款
    private Long userId; // 下单用户
    private Long addressBookId; // 地址id
    private LocalDateTime orderTime; // 下单时间
    private LocalDateTime checkoutTime; // 结账时间
    private Integer payMethod; // 支付方式 1微信,2支付宝
    private Short payStatus; // 支付状态 0未支付 1已支付 2退款
    private BigDecimal amount; // 实收金额
    private String remark; // 备注
    private String phone; // 手机号
    private String address; // 地址
    private String userName; // 用户名称
    private String consignee; // 收货人
    private String cancelReason; // 订单取消原因
    private String rejectionReason; // 订单拒绝原因
    private LocalDateTime cancelTime; // 订单取消时间
    private LocalDateTime estimatedDeliveryTime; // 预计送达时间
    private Short deliveryStatus; // 配送状态  1立即送出  0选择具体时间
    private LocalDateTime deliveryTime; // 送达时间
    private Integer packAmount; // 打包费
    private Integer tablewareNumber; // 餐具数量
    private Short tablewareStatus; // 餐具数量状态  1按餐量提供  0选择具体数量
}