package com.senda.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * 套餐
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Setmeal {

    private Long id; // 主键
    private Long categoryId; // 菜品分类id
    private String name; // 套餐名称
    private BigDecimal price; // 套餐价格
    private Integer status; // 售卖状态 0:停售 1:起售
    private String description; // 描述信息
    private String image; // 图片
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间
    private Long createUser; // 创建人
    private Long updateUser; // 修改人
}