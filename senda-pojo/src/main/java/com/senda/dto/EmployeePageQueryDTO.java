package com.senda.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * EmployeePageQuery DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeePageQueryDTO {

    private String name; // 员工姓名
    private int page; // 页码
    private int pageSize; // 每页显示记录数
}