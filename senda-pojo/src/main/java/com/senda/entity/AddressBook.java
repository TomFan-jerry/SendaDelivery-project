package com.senda.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * 地址簿
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressBook {

    private Long id; // 主键
    private Long userId; // 用户id
    private String consignee; // 收货人
    private String sex; // 性别
    private String phone; // 手机号
    private String provinceCode; // 省级区划编号
    private String provinceName; // 省级名称
    private String cityCode; // 市级区划编号
    private String cityName; // 市级名称
    private String districtCode; // 区级区划编号
    private String districtName; // 区级名称
    private String detail; // 详细地址
    private String label; // 标签
    private Short isDefault; // 默认 0 否 1是
}