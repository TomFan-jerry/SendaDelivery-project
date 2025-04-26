package com.senda.service.impl;

import com.senda.constant.MessageConstant;
import com.senda.constant.StatusConstant;
import com.senda.entity.Employee;
import com.senda.exceptions.AccountLockedException;
import com.senda.exceptions.AccountNotFoundException;
import com.senda.exceptions.PasswordErrorException;
import com.senda.mapper.EmployeeMapper;
import com.senda.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private PasswordEncoder passwordEncoder; // 直接注入加密器

    //员工登录
    @Override
    public Employee login(Employee employee) {
        // 1. 根据用户名查数据库
        Employee dbEmployee = employeeMapper.getByUsername(employee);

        if (dbEmployee == null) {
            // 用户不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        // 2. 校验密码
        boolean matches = passwordEncoder.matches(employee.getPassword(), dbEmployee.getPassword());

        if (!matches) {
            // 密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        //3.校验账号状态
        if (Objects.equals(dbEmployee.getStatus(), StatusConstant.DISABLE)) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        // 4. 登录成功，返回数据库查出来的完整员工信息
        return dbEmployee;
    }
}
