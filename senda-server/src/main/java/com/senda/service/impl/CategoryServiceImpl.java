package com.senda.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senda.dto.CategoryPageQueryDTO;
import com.senda.entity.Category;
import com.senda.mapper.CategoryMapper;
import com.senda.result.PageResult;
import com.senda.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    @Override
    public PageResult<Category> categoryPage(CategoryPageQueryDTO categoryPageQueryDTO) {
        //构建分页对象
        Page<Category> pagePram = new Page<>(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());

        //构建查询条件
        String name = categoryPageQueryDTO.getName();
        Integer type = categoryPageQueryDTO.getType();

        QueryWrapper<Category> categoryQueryWrapper = new QueryWrapper<Category>()
                .orderBy(false, true, "update_time");

        if (name != null && !name.trim().isEmpty()) { //若name不为null且不为空值则进行模糊查询
            categoryQueryWrapper.like("name", name);
        }
        if (type != null) { //若type不为null则进行查询
            categoryQueryWrapper.eq("type", type);
        }

        //执行查询
        Page<Category> page = categoryMapper.selectPage(pagePram, categoryQueryWrapper);

        //返回值封装
        PageResult<Category> pageResult = new PageResult<>();
        pageResult.setTotal(page.getTotal())
                .setRecords(page.getRecords());

        return pageResult;
    }
}
