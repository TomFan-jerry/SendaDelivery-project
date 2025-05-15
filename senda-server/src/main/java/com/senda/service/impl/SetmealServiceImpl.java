package com.senda.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.senda.annotation.AutoFill;
import com.senda.constant.MessageConstant;
import com.senda.constant.StatusConstant;
import com.senda.context.AutoFillEntityContext;
import com.senda.dto.SetmealDTO;
import com.senda.dto.SetmealPageQueryDTO;
import com.senda.entity.Category;
import com.senda.entity.Dish;
import com.senda.entity.Setmeal;
import com.senda.entity.SetmealDish;
import com.senda.enumeration.EntityType;
import com.senda.enumeration.OperationType;
import com.senda.exceptions.DeletionNotAllowedException;
import com.senda.mapper.SetmealMapper;
import com.senda.result.PageResult;
import com.senda.service.ISetmealService;
import com.senda.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements ISetmealService {

    /**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public PageResult<SetmealVO> setmealPage(SetmealPageQueryDTO setmealPageQueryDTO) {
        //构建分页对象
        Page<SetmealVO> pageParam = new Page<>(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());

        //执行查询
        Page<SetmealVO> page = baseMapper.pageSetmealWithCategory(pageParam, setmealPageQueryDTO);

        //返回值封装
        PageResult<SetmealVO> pageResult = new PageResult<>();
        pageResult.setTotal(page.getTotal())
                .setRecords(page.getRecords());

        return pageResult;
    }

    /**
     * 新增套餐
     * @param setmealDTO
     */
    @Override
    @AutoFill(operationType = OperationType.INSERT, entityType = EntityType.SETMEAL)
    @Transactional(rollbackFor = Exception.class)
    public void add(SetmealDTO setmealDTO) {
        //将数据拷贝到AOP中填充后的Setmeal实体对象
        Setmeal setmeal = (Setmeal) AutoFillEntityContext.getCurrentEntity();
        BeanUtils.copyProperties(setmealDTO, setmeal);

        //完善套餐信息
        setmeal.setStatus(StatusConstant.DISABLE);

        //插入setmeal数据
        save(setmeal);

        //获取被分配的id，完善菜品信息
        Long setmealId = setmeal.getId();

        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmealId));

        //插入setmealDish数据
        Db.saveBatch(setmealDishes);
    }

    /**
     * 批量删除套餐
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(List<Long> ids) {
        //若当前套餐为起售状态则抛出相应异常
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<Setmeal>()
                .in(Setmeal::getId, ids)
                .eq(Setmeal::getStatus, StatusConstant.ENABLE);

        if (!list(wrapper).isEmpty()) {
            throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
        }

        //删除套餐信息
        removeByIds(ids);

        //根据id删除相关菜品信息
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>(SetmealDish.class)
                .in(SetmealDish::getSetmealId, ids);

        Db.remove(queryWrapper);
    }

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @Override
    public SetmealVO selectById(Long id) {
        //查询套餐信息
        Setmeal setmeal = getById(id);

        //根据categoryId查询分类名称
        Category category = Db.lambdaQuery(Category.class)
                .eq(Category::getId, setmeal.getCategoryId())
                .one();
        String categoryName = category.getName();

        //根据id查询菜品信息
        List<SetmealDish> setmealDishes = Db.lambdaQuery(SetmealDish.class)
                .eq(SetmealDish::getSetmealId, id)
                .list();

        //返回值封装
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setCategoryName(categoryName);
        setmealVO.setSetmealDishes(setmealDishes);

        return setmealVO;
    }

    /**
     * 套餐起售、停售
     * @param setmealDTO
     */
    @Override
    public void setStatus(SetmealDTO setmealDTO) {
        //若当前套餐关联了停售菜品则抛出相应异常
        setmealDTO.setSetmealDishes(Db.lambdaQuery(SetmealDish.class)
                .eq(SetmealDish::getSetmealId, setmealDTO.getId())
                .list()); // 查询SetmealDish数据并封装到DTO

        List<Long> dishIds = setmealDTO.getSetmealDishes()
                .stream()
                .map(SetmealDish::getDishId)
                .toList(); // 拿到所有dishId

        if (!Db.lambdaQuery(Dish.class)
                .in(Dish::getId, dishIds)
                .eq(Dish::getStatus, StatusConstant.DISABLE)
                .list()
                .isEmpty()) {
            throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ENABLE_FAILED);
        } // 执行判断

        //将数据拷贝到AOP中填充后的Setmeal实体对象
        Setmeal setmeal = (Setmeal) AutoFillEntityContext.getCurrentEntity();
        BeanUtils.copyProperties(setmealDTO, setmeal);

        //更新数据
        updateById(setmeal);
    }
}
