package com.senda.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senda.dto.SetmealPageQueryDTO;
import com.senda.entity.Setmeal;
import com.senda.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SetmealMapper extends BaseMapper<Setmeal> {

    /**
     * 分页查询
     * @param pageParam
     * @param setmealPageQueryDTO
     * @return
     */
    Page<SetmealVO> pageSetmealWithCategory(Page<SetmealVO> pageParam, SetmealPageQueryDTO setmealPageQueryDTO);
}
