package com.senda.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senda.annotation.AutoFill;
import com.senda.constant.StatusConstant;
import com.senda.context.AutoFillEntityContext;
import com.senda.dto.CategoryDTO;
import com.senda.dto.CategoryPageQueryDTO;
import com.senda.entity.Category;
import com.senda.enumeration.EntityType;
import com.senda.enumeration.OperationType;
import com.senda.mapper.CategoryMapper;
import com.senda.result.PageResult;
import com.senda.service.ICategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

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

        LambdaQueryWrapper<Category> categoryQueryWrapper = new LambdaQueryWrapper<Category>()
                .orderByAsc(Category::getUpdateTime);

        if (name != null && !name.trim().isEmpty()) { //若name不为null且不为空值则进行模糊查询
            categoryQueryWrapper.like(Category::getName, name);
        }
        if (type != null) { //若type不为null则进行查询
            categoryQueryWrapper.eq(Category::getType, type);
        }

        //执行查询
        Page<Category> page = this.page(pagePram, categoryQueryWrapper);

        //返回值封装
        PageResult<Category> pageResult = new PageResult<>();
        pageResult.setTotal(page.getTotal())
                .setRecords(page.getRecords());

        return pageResult;
    }

    /**
     * 新增分类
     * @param categoryDTO
     */
    @Override
    @AutoFill(operationType = OperationType.INSERT, entityType = EntityType.CATEGORY)
    public void add(CategoryDTO categoryDTO) {
        //将数据拷贝到AOP中填充后的Category实体对象
        Category category = (Category) AutoFillEntityContext.getCurrentEntity();
        BeanUtils.copyProperties(categoryDTO, category);

        //完善分类信息
        category.setStatus(StatusConstant.ENABLE);

        //插入数据
        this.save(category);
    }

    /**
     * 根据类型查询分类
     * @param type
     * @return
     */
    @Override
    public List<Category> getByType(Integer type) {
        //构建查询条件
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<Category>()
                .eq(Category::getType, type);

        //执行查询并返回数据
        return this.list(queryWrapper);
    }
}
