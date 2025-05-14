package com.senda.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.senda.annotation.AutoFill;
import com.senda.constant.StatusConstant;
import com.senda.context.AutoFillEntityContext;
import com.senda.dto.SetmealDTO;
import com.senda.dto.SetmealPageQueryDTO;
import com.senda.entity.Category;
import com.senda.entity.Setmeal;
import com.senda.entity.SetmealDish;
import com.senda.enumeration.EntityType;
import com.senda.enumeration.OperationType;
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
    @Transactional
    public void add(SetmealDTO setmealDTO) {
        //将数据拷贝到AOP中填充后的Setmeal实体对象
        Setmeal setmeal = (Setmeal) AutoFillEntityContext.getCurrentEntity();
        BeanUtils.copyProperties(setmealDTO, setmeal);

        //完善套餐信息
        setmeal.setStatus(StatusConstant.ENABLE);

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
}
