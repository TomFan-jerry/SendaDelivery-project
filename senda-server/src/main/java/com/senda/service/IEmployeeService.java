package com.senda.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.senda.dto.*;
import com.senda.entity.Employee;
import com.senda.result.PageResult;

public interface IEmployeeService extends IService<Employee> {

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
     * 修改密码
     * @param employeeEditPasswordDTO
     */
    void editPassword(EmployeeEditPasswordDTO employeeEditPasswordDTO);
}
