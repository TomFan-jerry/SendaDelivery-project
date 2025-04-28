package com.senda.controller.admin;

import com.senda.dto.CategoryPageQueryDTO;
import com.senda.entity.Category;
import com.senda.result.PageResult;
import com.senda.result.Result;
import com.senda.service.CategoryService;
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
    private CategoryService categoryService;

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult<Category>> categoryPage(CategoryPageQueryDTO categoryPageQueryDTO) {
        log.info("分类分页查询:{}", categoryPageQueryDTO);
        PageResult<Category> pageResult = categoryService.categoryPage(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

}
