package com.senda.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.senda.dto.DishPageQueryDTO;
import com.senda.entity.Dish;
import com.senda.result.PageResult;
import com.senda.vo.DishVO;

public interface IDishService extends IService<Dish> {

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    PageResult<DishVO> dishPage(DishPageQueryDTO dishPageQueryDTO);
}
