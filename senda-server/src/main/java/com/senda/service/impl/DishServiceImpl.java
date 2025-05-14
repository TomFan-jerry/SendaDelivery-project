package com.senda.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.senda.constant.StatusConstant;
import com.senda.context.AutoFillEntityContext;
import com.senda.dto.DishDTO;
import com.senda.dto.DishPageQueryDTO;
import com.senda.entity.Category;
import com.senda.entity.Dish;
import com.senda.entity.DishFlavor;
import com.senda.mapper.DishMapper;
import com.senda.result.PageResult;
import com.senda.service.IDishService;
import com.senda.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements IDishService {

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

    /**
     * 新增菜品
     * @param dishDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(DishDTO dishDTO) {
        //将数据拷贝到AOP中填充后的Dish实体对象
        Dish dish = (Dish) AutoFillEntityContext.getCurrentEntity();
        BeanUtils.copyProperties(dishDTO, dish);

        //完善菜品信息
        dish.setStatus(StatusConstant.ENABLE);

        //插入dish数据
        this.save(dish);

        //获取被分配的id，完善口味信息
        Long dishId = dish.getId();

        List<DishFlavor> flavors = dishDTO.getFlavors();
        flavors.forEach(dishFlavor -> dishFlavor.setDishId(dishId));

        //插入dishFlavor数据
        Db.saveBatch(flavors);
    }

    /**
     * 批量删除菜品
     * @param ids
     */
    @Override
    @Transactional
    public void deleteByIds(List<Long> ids) {
        //删除菜品信息
        removeByIds(ids);

        //根据id删除相关口味信息
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>(DishFlavor.class)
                .in(DishFlavor::getDishId, ids);

        Db.remove(queryWrapper);
    }

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    @Override
    public DishVO selectById(Long id) {
        //查询菜品信息
        Dish dish = getById(id);

        //根据categoryId查询分类名称
        Category category = Db.lambdaQuery(Category.class)
                .eq(Category::getId, dish.getCategoryId())
                .one();
        String categoryName = category.getName();

        //根据id查询口味信息
        List<DishFlavor> dishFlavors = Db.lambdaQuery(DishFlavor.class)
                .eq(DishFlavor::getDishId, id)
                .list();

        //返回值封装
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setCategoryName(categoryName);
        dishVO.setFlavors(dishFlavors);

        return dishVO;
    }

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @Override
    public List<Dish> selectByCategoryId(Long categoryId) {
        //构建查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<Dish>()
                .eq(Dish::getCategoryId, categoryId);

        //执行查询并返回数据
        return list(queryWrapper);
    }
}
