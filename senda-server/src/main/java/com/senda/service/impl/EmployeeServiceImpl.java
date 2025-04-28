package com.senda.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senda.constant.MessageConstant;
import com.senda.constant.PasswordConstant;
import com.senda.constant.StatusConstant;
import com.senda.context.JwtUserContext;
import com.senda.dto.*;
import com.senda.entity.Employee;
import com.senda.exceptions.AccountLockedException;
import com.senda.exceptions.AccountNotFoundException;
import com.senda.exceptions.PasswordErrorException;
import com.senda.mapper.EmployeeMapper;
import com.senda.result.PageResult;
import com.senda.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private Employee employee;

    @Autowired
    private EmployeeMapper employeeMapper;

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
        Employee dbEmployee = employeeMapper.getByUsername(employeeLoginDTO.getUsername());
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
    public void add(EmployeeDTO employeeDTO) {
        //对象属性拷贝
        BeanUtils.copyProperties(employeeDTO, employee);

        //完善员工信息
        employee.setStatus(StatusConstant.ENABLE) //帐号状态
                .setPassword(passwordEncoder.encode(PasswordConstant.DEFAULT_PASSWORD)) //密码加密
                .setCreateTime(LocalDateTime.now()) //创建时间
                .setUpdateTime(LocalDateTime.now()) //修改时间
                .setCreateUser(JwtUserContext.getCurrentUserId()) //创建人
                .setUpdateUser(JwtUserContext.getCurrentUserId()); //修改人

        //插入数据
        employeeMapper.insert(employee);
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

        QueryWrapper<Employee> employeeQueryWrapper = new QueryWrapper<Employee>()
                .orderBy(false, false, "update_time");

        if (name != null && !name.trim().isEmpty()) { //若name不为null且不为空值则进行模糊查询
            employeeQueryWrapper.like("name", name);
        }

        //执行查询
        Page<Employee> page = employeeMapper.selectPage(pagePram, employeeQueryWrapper);

        //返回值封装
        PageResult<Employee> pageResult = new PageResult<>();
        pageResult.setTotal(page.getTotal())
                .setRecords(page.getRecords());

        return pageResult;
    }

    /**
     * 修改员工账号状态
     * @param employeeStatusDTO
     */
    @Override
    public void setStatus(EmployeeStatusDTO employeeStatusDTO) {
        //对象属性拷贝
        BeanUtils.copyProperties(employeeStatusDTO, employee);

        //完善员工信息
        employee.setUpdateTime(LocalDateTime.now()) //修改时间
                .setUpdateUser(JwtUserContext.getCurrentUserId()); //修改人

        //更新数据
        employeeMapper.updateById(employee);
    }

    /**
     * 根据id查询员工
     * @param id
     * @return
     */
    @Override
    public Employee selectById(Long id) {
        //执行查询
        Employee result = employeeMapper.selectById(id);
        return result;
    }

    /**
     * 修改员工信息
     * @param employeeDTO
     */
    @Override
    public void update(EmployeeDTO employeeDTO) {
        //对象属性拷贝
        BeanUtils.copyProperties(employeeDTO, employee);

        //完善员工信息
        employee.setUpdateTime(LocalDateTime.now()) //修改时间
                .setUpdateUser(JwtUserContext.getCurrentUserId()); //修改人

        //更新数据
        employeeMapper.updateById(employee);
    }

    /**
     * 修改密码
     * @param employeeEditPasswordDTO
     */
    @Override
    public void editPassword(EmployeeEditPasswordDTO employeeEditPasswordDTO) {
        // 先验证原密码是否正确（哈希加密算法不可逆，无法直接查询比对）
        if (!passwordEncoder.matches(
                employeeEditPasswordDTO.getOldPassword(),
                employeeMapper.selectById(
                        JwtUserContext.getCurrentUserId()
                ).getPassword())
        ) {
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);  //原始密码错误抛出异常
        }

        //新密码加密
        employeeEditPasswordDTO.setNewPassword(passwordEncoder.encode(employeeEditPasswordDTO.getNewPassword()));

        //构建修改条件
        UpdateWrapper<Employee> wrapper = new UpdateWrapper<Employee>()
                .set("password", employeeEditPasswordDTO.getNewPassword())
                .set("update_time", LocalDateTime.now())
                .set("update_user", JwtUserContext.getCurrentUserId())
                .eq("id", JwtUserContext.getCurrentUserId());

        //执行修改
        employeeMapper.update(wrapper);
    }
}
