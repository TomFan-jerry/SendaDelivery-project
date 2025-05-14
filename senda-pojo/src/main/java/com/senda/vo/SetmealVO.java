package com.senda.vo;

import com.senda.entity.Dish;
import com.senda.entity.SetmealDish;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Setmeal VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SetmealVO {

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
    private String categoryName; // 套餐分类名称
    private List<SetmealDish> setmealDishes; // 套餐包含的菜品
}