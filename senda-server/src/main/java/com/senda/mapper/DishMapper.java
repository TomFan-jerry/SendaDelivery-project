package com.senda.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senda.dto.DishPageQueryDTO;
import com.senda.entity.Dish;
import com.senda.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {

    /**
     * 菜品分页查询
     * @param pageParam
     * @param dishPageQueryDTO
     * @return
     */
    Page<DishVO> pageDishWithCategory(Page<DishVO> pageParam, DishPageQueryDTO dishPageQueryDTO);
}
