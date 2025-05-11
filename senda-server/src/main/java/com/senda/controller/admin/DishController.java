package com.senda.controller.admin;

import com.senda.dto.DishPageQueryDTO;
import com.senda.entity.Dish;
import com.senda.result.PageResult;
import com.senda.result.Result;
import com.senda.service.IDishService;
import com.senda.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
