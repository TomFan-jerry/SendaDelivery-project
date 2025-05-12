package com.senda.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.senda.dto.DishDTO;
import com.senda.dto.DishPageQueryDTO;
import com.senda.entity.Dish;
import com.senda.result.PageResult;
import com.senda.vo.DishVO;

import java.util.List;

public interface IDishService extends IService<Dish> {

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    PageResult<DishVO> dishPage(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 新增菜品
     * @param dishDTO
     */
    void add(DishDTO dishDTO);

    /**
     * 批量删除菜品
     * @param ids
     */
    void deleteByIds(List<Long> ids);

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    DishVO selectById(Long id);

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    List<Dish> selectByCategoryId(Long categoryId);
}
