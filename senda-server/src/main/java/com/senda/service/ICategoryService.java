package com.senda.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.senda.dto.CategoryPageQueryDTO;
import com.senda.entity.Category;
import com.senda.result.PageResult;

public interface ICategoryService extends IService<Category> {

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    PageResult<Category> categoryPage(CategoryPageQueryDTO categoryPageQueryDTO);
}
