package com.senda.controller.admin;

import com.senda.dto.CategoryPageQueryDTO;
import com.senda.entity.Category;
import com.senda.result.PageResult;
import com.senda.result.Result;
import com.senda.service.ICategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/admin/category")
public class CategoryController {

    @Autowired
    private ICategoryService ICategoryService;

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult<Category>> categoryPage(CategoryPageQueryDTO categoryPageQueryDTO) {
        log.info("分类分页查询:{}", categoryPageQueryDTO);
        PageResult<Category> pageResult = ICategoryService.categoryPage(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

}
