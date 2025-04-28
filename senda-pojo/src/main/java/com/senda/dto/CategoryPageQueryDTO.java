package com.senda.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * CategoryPageQueryDTO DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryPageQueryDTO {

    private Integer type; // 分类类型：1为菜品分类，2为套餐分类
    private String name; // 分类名称
    private int page; // 页码
    private int pageSize; // 每页显示记录数
}
