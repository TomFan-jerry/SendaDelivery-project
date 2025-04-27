package com.senda.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senda.constant.MessageConstant;
import com.senda.constant.PasswordConstant;
import com.senda.constant.StatusConstant;
import com.senda.dto.EmployeeDTO;
import com.senda.dto.EmployeeLoginDTO;
import com.senda.entity.Employee;
import com.senda.exceptions.AccountLockedException;
import com.senda.exceptions.AccountNotFoundException;
import com.senda.exceptions.PasswordErrorException;
import com.senda.mapper.EmployeeMapper;
import com.senda.result.PageResult;
import com.senda.service.EmployeeService;
import com.senda.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private HttpServletRequest httpServletRequest;

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
        Employee employee = new Employee();

        //对象属性拷贝
        BeanUtils.copyProperties(employeeDTO, employee);

        //获取操作人id，即当前登陆的员工的id（获取请求头中的jwt令牌并解析）
        String jwt = httpServletRequest.getHeader("token");
        Claims claims = JwtUtils.parseToken(jwt);

        //完善员工信息
        employee.setStatus(StatusConstant.ENABLE) //帐号状态
                .setPassword(passwordEncoder.encode(PasswordConstant.DEFAULT_PASSWORD)) //密码加密
                .setCreateTime(LocalDateTime.now()) //创建时间
                .setUpdateTime(LocalDateTime.now()) //修改时间
                .setCreateUser(Long.valueOf(claims.get("id").toString())) //创建人
                .setUpdateUser(Long.valueOf(claims.get("id").toString())); //修改人

        employeeMapper.insert(employee);
    }

    //员工分页查询
    @Override
    public PageResult<Employee> employeePage(String name, Integer page, Integer pageSize) {
        //构建分页对象
        Page<Employee> pagePram = new Page<>(page, pageSize);

        //构建查询条件
        QueryWrapper<Employee> employeeQueryWrapper = new QueryWrapper<Employee>()
                .orderBy(false, false, "update_time");
        if (name != null && !name.trim().isEmpty()) { //若name不为null且不为空值则进行模糊查询
            employeeQueryWrapper.like("name", name);
        }

        //执行查询
        Page<Employee> result = employeeMapper.selectPage(pagePram, employeeQueryWrapper);

        //返回值封装
        PageResult<Employee> pageResult = new PageResult<Employee>();
        pageResult.setTotal(result.getTotal());
        pageResult.setRecords(result.getRecords());

        return pageResult;
    }

}
