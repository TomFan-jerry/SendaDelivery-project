package com.senda.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.senda.annotation.AutoFill;
import com.senda.constant.MessageConstant;
import com.senda.constant.PasswordConstant;
import com.senda.constant.StatusConstant;
import com.senda.context.JwtUserContext;
import com.senda.dto.*;
import com.senda.entity.Employee;
import com.senda.enumeration.OperationType;
import com.senda.exceptions.AccountLockedException;
import com.senda.exceptions.AccountNotFoundException;
import com.senda.exceptions.PasswordErrorException;
import com.senda.mapper.EmployeeMapper;
import com.senda.result.PageResult;
import com.senda.service.IEmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {

    @Autowired
    private Employee employee;

    @Autowired
    private PasswordEncoder passwordEncoder; // 直接注入加密器

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    @Override
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        // 1. 根据用户名查数据库
        Employee dbEmployee = lambdaQuery()
                .eq(Employee::getUsername, employeeLoginDTO.getUsername())
                .one();
        if (dbEmployee == null) {
            // 用户不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        // 2. 校验密码
        boolean matches = passwordEncoder.matches(employeeLoginDTO.getPassword(), dbEmployee.getPassword());
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

    /**
     * 新增员工
     * @param employeeDTO
     */
    @Override
    @AutoFill(value = OperationType.INSERT)
    public void add(EmployeeDTO employeeDTO) {
        //对象属性拷贝
        BeanUtils.copyProperties(employeeDTO, employee);

        //完善员工信息
        employee.setStatus(StatusConstant.ENABLE) //帐号状态
                .setPassword(passwordEncoder.encode(PasswordConstant.DEFAULT_PASSWORD)); //密码加密

        //插入数据
        this.save(employee);
    }

    /**
     * 员工分页查询
     * @param employeePageQueryDTO
     * @return
     */
    @Override
    public PageResult<Employee> employeePage(EmployeePageQueryDTO employeePageQueryDTO) {
        //构建分页对象
        Page<Employee> pagePram = new Page<>(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());

        //构建查询条件
        String name = employeePageQueryDTO.getName();

        LambdaQueryWrapper<Employee> employeeQueryWrapper = new LambdaQueryWrapper<Employee>()
                .orderByAsc(Employee::getCreateTime);

        if (name != null && !name.trim().isEmpty()) { //若name不为null且不为空值则进行模糊查询
            employeeQueryWrapper.like(Employee::getName, name);
        }

        //执行查询
        Page<Employee> page = this.page(pagePram, employeeQueryWrapper);

        //返回值封装
        PageResult<Employee> pageResult = new PageResult<>();
        pageResult.setTotal(page.getTotal())
                .setRecords(page.getRecords());

        return pageResult;
    }

    /**
     * 修改密码
     * @param employeeEditPasswordDTO
     */
    @Override
    @AutoFill(value = OperationType.UPDATE)
    public void editPassword(EmployeeEditPasswordDTO employeeEditPasswordDTO) {
        // 先验证原密码是否正确（哈希加密算法不可逆，无法直接查询比对）
        if (!passwordEncoder.matches(
                employeeEditPasswordDTO.getOldPassword(),
                this.getById(
                        JwtUserContext.getCurrentUserId()
                ).getPassword())
        ) {
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);  //原始密码错误抛出异常
        }

        //新密码加密
        employeeEditPasswordDTO.setNewPassword(passwordEncoder.encode(employeeEditPasswordDTO.getNewPassword()));

        //执行修改
        this.lambdaUpdate()
                .set(Employee::getPassword, employeeEditPasswordDTO.getNewPassword())
                .set(Employee::getUpdateTime, LocalDateTime.now())
                .set(Employee::getUpdateUser, JwtUserContext.getCurrentUserId())
                .eq(Employee::getId, JwtUserContext.getCurrentUserId())
                .update();
    }
}
