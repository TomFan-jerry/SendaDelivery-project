package com.senda.service;

import com.senda.dto.EmployeeDTO;
import com.senda.dto.EmployeeLoginDTO;
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

    //员工分页查询
    PageResult<Employee> employeePage(String name, Integer page, Integer pageSize);


}
