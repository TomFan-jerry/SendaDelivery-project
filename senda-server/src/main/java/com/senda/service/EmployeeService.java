package com.senda.service;

import com.senda.dto.EmployeeDTO;
import com.senda.dto.EmployeeLoginDTO;
import com.senda.dto.EmployeePageQueryDTO;
import com.senda.dto.EmployeeStatusDTO;
import com.senda.entity.Employee;
import com.senda.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 新增员工
     * @param employeeDTO
     */
    void add(EmployeeDTO employeeDTO);

    /**
     * 员工分页查询
     * @param employeePageQueryDTO
     * @return
     */
    PageResult<Employee> employeePage(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 修改员工账号状态
     * @param employeeStatusDTO
     */
    void setStatus(EmployeeStatusDTO employeeStatusDTO);
}
