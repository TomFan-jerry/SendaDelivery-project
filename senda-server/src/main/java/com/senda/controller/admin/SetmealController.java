package com.senda.controller.admin;

import com.senda.annotation.AutoFill;
import com.senda.context.AutoFillEntityContext;
import com.senda.dto.SetmealDTO;
import com.senda.dto.SetmealPageQueryDTO;
import com.senda.entity.Setmeal;
import com.senda.enumeration.EntityType;
import com.senda.enumeration.OperationType;
import com.senda.result.PageResult;
import com.senda.result.Result;
import com.senda.service.ISetmealService;
import com.senda.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/setmeal")
public class SetmealController {

    @Autowired
    private ISetmealService setmealService;

    /**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult<SetmealVO>> setmealPage(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("分页查询:{}", setmealPageQueryDTO);
        PageResult<SetmealVO> pageResult = setmealService.setmealPage(setmealPageQueryDTO);
        return Result.success(null, pageResult);
    }

    /**
     * 新增套餐
     * @param setmealDTO
     * @return
     */
    @PostMapping
    public Result<SetmealDTO> add(@RequestBody SetmealDTO setmealDTO) {
        log.info("新增套餐:{}", setmealDTO);
        setmealService.add(setmealDTO);
        return Result.success(setmealDTO);
    }

    /**
     * 批量删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result<String> deleteByIds(@RequestParam List<Long> ids) {
        log.info("批量删除套餐:{}", ids);
        setmealService.removeByIds(ids);
        return Result.success(ids.toString());
    }

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<SetmealVO> selectById(@PathVariable Long id) {
        log.info("根据id查询套餐:{}", id);
        SetmealVO setmealVO = setmealService.selectById(id);
        return Result.success(setmealVO);
    }

    /**
     * 修改套餐
     * @param setmealDTO
     * @return
     */
    @PutMapping
    @AutoFill(operationType = OperationType.UPDATE, entityType = EntityType.SETMEAL)
    public Result<String> update(@RequestBody SetmealDTO setmealDTO) {
        log.info("修改套餐:{}", setmealDTO);

        //将数据拷贝到AOP中填充后的Setmeal实体对象
        Setmeal setmeal = (Setmeal) AutoFillEntityContext.getCurrentEntity();
        BeanUtils.copyProperties(setmealDTO, setmeal);

        //更新数据
        setmealService.updateById(setmeal);

        return Result.success(setmealDTO.getId().toString());
    }

    /**
     * 套餐起售、停售
     * @param setmealDTO
     * @return
     */
    @PostMapping("/status/{status}")
    @AutoFill(operationType = OperationType.UPDATE, entityType = EntityType.SETMEAL)
    public Result<String> setStatus(SetmealDTO setmealDTO) {
        log.info("套餐起售、停售:{}", setmealDTO);

        //将数据拷贝到AOP中填充后的Setmeal实体对象
        Setmeal setmeal = (Setmeal) AutoFillEntityContext.getCurrentEntity();
        BeanUtils.copyProperties(setmealDTO, setmeal);

        //更新数据
        setmealService.updateById(setmeal);

        return Result.success(setmeal.getStatus().toString());
    }

}
