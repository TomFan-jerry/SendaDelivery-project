package com.senda.controller.admin;

import com.senda.annotation.AutoFill;
import com.senda.context.AutoFillEntityContext;
import com.senda.dto.DishDTO;
import com.senda.dto.DishPageQueryDTO;
import com.senda.entity.Dish;
import com.senda.enumeration.EntityType;
import com.senda.enumeration.OperationType;
import com.senda.result.PageResult;
import com.senda.result.Result;
import com.senda.service.IDishService;
import com.senda.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/dish")
public class DishController {

    @Autowired
    private IDishService dishService;

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult<DishVO>> dishPage(DishPageQueryDTO dishPageQueryDTO) {
        log.info("菜品分页查询:{}", dishPageQueryDTO);
        PageResult<DishVO> pageResult = dishService.dishPage(dishPageQueryDTO);
        return Result.success(null, pageResult);
    }

    /**
     * 新增菜品
     * @param dishDTO
     * @return
     */
    @PostMapping
    @AutoFill(operationType = OperationType.INSERT, entityType = EntityType.DISH)
    public Result<String> add(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品:{}", dishDTO);
        dishService.add(dishDTO);
        return Result.success(dishDTO.toString());
    }

    /**
     * 批量删除菜品
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result<String> deleteByIds(@RequestParam List<Long> ids) {
        log.info("批量删除菜品:{}", ids);
        dishService.deleteByIds(ids);
        return Result.success(ids.toString());
    }

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<DishVO> selectById(@PathVariable Long id) {
        log.info("根据id查询菜品:{}", id);
        DishVO dishVO = dishService.selectById(id);
        return Result.success(dishVO);
    }

    /**
     * 修改菜品
     * @param dishDTO
     * @return
     */
    @PutMapping
    @AutoFill(operationType = OperationType.UPDATE, entityType = EntityType.DISH)
    public Result<String> update(@RequestBody DishDTO dishDTO) {
        log.info("修改菜品:{}", dishDTO);

        //将数据拷贝到AOP中填充后的Dish实体对象
        Dish dish = (Dish) AutoFillEntityContext.getCurrentEntity();
        BeanUtils.copyProperties(dishDTO, dish);

        //更新数据
        dishService.updateById(dish);

        return Result.success(dishDTO.getId().toString());
    }

    /**
     * 菜品起售、停售
     * @param dishDTO
     * @return
     */
    @PostMapping("/status/{status}")
    @AutoFill(operationType = OperationType.UPDATE, entityType = EntityType.DISH)
    public Result<String> setStatus(DishDTO dishDTO) {
        log.info("菜品起售、停售:{}", dishDTO);

        //将数据拷贝到AOP中填充后的Dish实体对象
        Dish dish = (Dish) AutoFillEntityContext.getCurrentEntity();
        BeanUtils.copyProperties(dishDTO, dish);

        //更新数据
        dishService.updateById(dish);

        return Result.success(dishDTO.getStatus().toString());
    }

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    public Result<List<Dish>> selectByCategoryId(Long categoryId) {
        log.info("根据分类id查询菜品:{}", categoryId);
        List<Dish> dishes = dishService.selectByCategoryId(categoryId);
        return Result.success(dishes);
    }

}
