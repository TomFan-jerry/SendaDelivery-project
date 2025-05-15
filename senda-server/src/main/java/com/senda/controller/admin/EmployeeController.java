package com.senda.controller.admin;

import com.senda.annotation.AutoFill;
import com.senda.context.AutoFillEntityContext;
import com.senda.dto.*;
import com.senda.entity.Employee;
import com.senda.enumeration.EntityType;
import com.senda.enumeration.OperationType;
import com.senda.enumeration.TokenType;
import com.senda.result.PageResult;
import com.senda.result.Result;
import com.senda.service.IEmployeeService;
import com.senda.utils.JwtUtil;
import com.senda.vo.EmployeeLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/admin/employee")
public class EmployeeController {

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录:{}", employeeLoginDTO);
        Employee employee = employeeService.login(employeeLoginDTO);

        //生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", employee.getId());
        claims.put("name", employee.getName());
        claims.put("username", employee.getUsername());
        String jwt = jwtUtil.generateToken(claims, TokenType.ADMIN);

        //封装前端返回值
        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(jwt)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 员工登出
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        log.info("员工登出");
        return Result.success("已退出");
    }

    /**
     * 新增员工
     * @param employeeDTO
     * @return
     */
    @PostMapping
    public Result<String> add(@RequestBody EmployeeDTO employeeDTO) {
        log.info("新增员工:{}", employeeDTO);
        employeeService.add(employeeDTO);
        return Result.success(employeeDTO.toString());
    }

    /**
     * 员工分页查询
     * @param employeePageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult<Employee>> employeePage(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("员工分页查询:{}", employeePageQueryDTO);
        PageResult<Employee> pageResult = employeeService.employeePage(employeePageQueryDTO);
        return Result.success(null, pageResult);
    }

    /**
     * 修改员工账号状态
     * @param employeeDTO
     * @return
     */
    @PostMapping("/status/{status}")
    @AutoFill(operationType = OperationType.UPDATE, entityType = EntityType.EMPLOYEE)
    public Result<String> setStatus(EmployeeDTO employeeDTO) {
        log.info("修改员工账号状态:{}", employeeDTO);

        //将数据拷贝到AOP中填充后的Employee实体对象
        Employee employee = (Employee) AutoFillEntityContext.getCurrentEntity();
        BeanUtils.copyProperties(employeeDTO, employee);

        //更新数据
        employeeService.updateById(employee);

        return Result.success(employeeDTO.getStatus().toString());
    }

    /**
     * 根据id查询员工
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Employee> selectById(@PathVariable Long id) {
        log.info("根据id查询员工:{}", id);
        Employee employee = employeeService.getById(id);
        return Result.success(employee);
    }

    /**
     * 修改员工信息
     * @param employeeDTO
     * @return
     */
    @PutMapping
    @AutoFill(operationType = OperationType.UPDATE, entityType = EntityType.EMPLOYEE)
    public Result<String> update(@RequestBody EmployeeDTO employeeDTO) {
        log.info("修改员工信息:{}", employeeDTO);

        //将数据拷贝到AOP中填充后的Employee实体对象
        Employee employee = (Employee) AutoFillEntityContext.getCurrentEntity();
        BeanUtils.copyProperties(employeeDTO, employee);

        //更新数据
        employeeService.updateById(employee);

        return Result.success(employeeDTO.getId().toString());
    }

    /**
     * 修改密码
     * @param employeeEditPasswordDTO
     * @return
     */
    @PutMapping("/editPassword")
    public Result<String> editPassword(@RequestBody EmployeeEditPasswordDTO employeeEditPasswordDTO) {
        log.info("修改密码:{}", employeeEditPasswordDTO);
        employeeService.editPassword(employeeEditPasswordDTO);
        return Result.success(employeeEditPasswordDTO.getNewPassword());
    }
}
