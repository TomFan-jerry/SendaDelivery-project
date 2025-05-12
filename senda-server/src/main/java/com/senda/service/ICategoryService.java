package com.senda.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.senda.dto.CategoryDTO;
import com.senda.dto.CategoryPageQueryDTO;
import com.senda.entity.Category;
import com.senda.result.PageResult;

import java.util.List;

public interface ICategoryService extends IService<Category> {

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    PageResult<Category> categoryPage(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 新增分类
     * @param categoryDTO
     */
    void add(CategoryDTO categoryDTO);

    /**
     *根据类型查询分类
     * @param type
     * @return
     */
    List<Category> getByType(Integer type);
}
