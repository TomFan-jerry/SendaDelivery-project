package com.senda.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;

/**
 * 菜品及套餐分类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    private Long id; // 主键
    private Integer type; // 类型   1 菜品分类 2 套餐分类
    private String name; // 分类名称
    private Integer sort; // 顺序
    private Integer status; // 分类状态 0:禁用，1:启用
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间
    private Long createUser; // 创建人
    private Long updateUser; // 修改人
}