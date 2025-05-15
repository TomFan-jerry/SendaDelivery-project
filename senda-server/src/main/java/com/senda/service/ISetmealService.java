package com.senda.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.senda.dto.SetmealDTO;
import com.senda.dto.SetmealPageQueryDTO;
import com.senda.entity.Setmeal;
import com.senda.result.PageResult;
import com.senda.vo.SetmealVO;

import java.util.List;

public interface ISetmealService extends IService<Setmeal> {

    /**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    PageResult<SetmealVO> setmealPage(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 新增套餐
     * @param setmealDTO
     */
    void add(SetmealDTO setmealDTO);

    /**
     * 批量删除套餐
     * @param ids
     */
    void deleteByIds(List<Long> ids);

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    SetmealVO selectById(Long id);

    /**
     * 套餐起售、停售
     * @param setmealDTO
     */
    void setStatus(SetmealDTO setmealDTO);
}
