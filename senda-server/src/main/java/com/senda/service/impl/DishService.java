package com.senda.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senda.dto.DishPageQueryDTO;
import com.senda.entity.Dish;
import com.senda.mapper.DishMapper;
import com.senda.result.PageResult;
import com.senda.service.ICategoryService;
import com.senda.service.IDishService;
import com.senda.vo.DishVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DishService extends ServiceImpl<DishMapper, Dish> implements IDishService {

    @Autowired
    private ICategoryService categoryService;

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult<DishVO> dishPage(DishPageQueryDTO dishPageQueryDTO) {
        //构建分页对象
        Page<DishVO> pageParam = new Page<>(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());

        //执行查询
        Page<DishVO> page = baseMapper.pageDishWithCategory(pageParam, dishPageQueryDTO);

        //返回值封装
        PageResult<DishVO> pageResult = new PageResult<>();
        pageResult.setTotal(page.getTotal())
                .setRecords(page.getRecords());

        return pageResult;
    }
}
