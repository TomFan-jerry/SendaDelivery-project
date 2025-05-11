package com.senda.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * DishPageQuery DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DishPageQueryDTO {

    private String name; // name
    private Long categoryId; // categoryId
    private Integer status; // status
    private int page; // 页码
    private int pageSize; // 每页显示记录数
}
