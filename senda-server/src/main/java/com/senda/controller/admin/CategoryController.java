package com.senda.controller.admin;

import com.senda.annotation.AutoFill;
import com.senda.constant.AutoFillConstant;
import com.senda.context.AutoFillEntityContext;
import com.senda.dto.CategoryDTO;
import com.senda.dto.CategoryPageQueryDTO;
import com.senda.dto.CategoryStatusDTO;
import com.senda.entity.Category;
import com.senda.enumeration.EntityType;
import com.senda.enumeration.OperationType;
import com.senda.result.PageResult;
import com.senda.result.Result;
import com.senda.service.ICategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin/category")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult<Category>> categoryPage(CategoryPageQueryDTO categoryPageQueryDTO) {
        log.info("分类分页查询:{}", categoryPageQueryDTO);
        PageResult<Category> pageResult = categoryService.categoryPage(categoryPageQueryDTO);
        return Result.success(null, pageResult);
    }

    /**
     * 新增分类
     * @param categoryDTO
     * @return
     */
    @PostMapping
    public Result<String> add(@RequestBody CategoryDTO categoryDTO) {
        log.info("新增分类:{}", categoryDTO);
        categoryService.add(categoryDTO);
        return Result.success(categoryDTO.toString());
    }

    /**
     * 根据id删除分类
     * @param id
     * @return
     */
    @DeleteMapping
    public Result<String> deleteById(Long id) {
        log.info("根据id删除分类:{}", id);
        categoryService.removeById(id);
        return Result.success(id.toString());
    }

    /**
     * 修改分类
     * @param categoryDTO
     * @return
     */
    @PutMapping
    @AutoFill(operationType = OperationType.UPDATE, entityType = EntityType.CATEGORY)
    public Result<String> update(@RequestBody CategoryDTO categoryDTO){
        log.info("修改分类:{}", categoryDTO);

        //将数据拷贝到AOP中填充后的Employee实体对象
        Category category = (Category) AutoFillEntityContext.getCurrentEntity();
        BeanUtils.copyProperties(categoryDTO, category);

        //更新数据
        categoryService.updateById(category);

        return Result.success(categoryDTO.getId().toString());
    }

    /**
     * 启用、禁用分类
     * @param categoryStatusDTO
     * @return
     */
    @PostMapping("/status/{status}")
    @AutoFill(operationType = OperationType.UPDATE, entityType = EntityType.CATEGORY)
    public Result<String> setStatus(CategoryStatusDTO categoryStatusDTO) {
        log.info("启用、禁用分类:{}", categoryStatusDTO);

        //将数据拷贝到AOP中填充后的Employee实体对象
        Category category = (Category) AutoFillEntityContext.getCurrentEntity();
        BeanUtils.copyProperties(categoryStatusDTO, category);

        //更新数据
        categoryService.updateById(category);

        return Result.success(categoryStatusDTO.getStatus().toString());
    }

}
