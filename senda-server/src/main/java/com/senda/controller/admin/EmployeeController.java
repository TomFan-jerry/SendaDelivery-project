package com.senda.controller.admin;

import com.senda.context.JwtUserContext;
import com.senda.dto.*;
import com.senda.entity.Employee;
import com.senda.result.PageResult;
import com.senda.result.Result;
import com.senda.service.IEmployeeService;
import com.senda.utils.JwtUtils;
import com.senda.vo.EmployeeLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/admin/employee")
public class EmployeeController {

    @Autowired
    private IEmployeeService employeeService;

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
        String jwt = JwtUtils.generateToken(claims);

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
    public Result<EmployeeDTO> add(@RequestBody EmployeeDTO employeeDTO) {
        log.info("新增员工:{}", employeeDTO);
        employeeService.add(employeeDTO);
        return Result.success(employeeDTO);
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
     * @param status
     * @param employeeStatusDTO
     * @return
     */
    @PostMapping("/status/{status}")
    public Result<String> setStatus(@PathVariable Integer status, EmployeeStatusDTO employeeStatusDTO) {
        employeeStatusDTO.setStatus(status);
        log.info("修改员工账号状态:{}", employeeStatusDTO);

        //对象属性拷贝
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeStatusDTO, employee);

        //完善员工信息
        employee.setUpdateTime(LocalDateTime.now()) //修改时间
                .setUpdateUser(JwtUserContext.getCurrentUserId()); //修改人

        //更新数据
        employeeService.updateById(employee);

        return Result.success(status.toString());
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
    public Result<String> update(@RequestBody EmployeeDTO employeeDTO) {
        log.info("修改员工信息:{}", employeeDTO);

        //对象属性拷贝
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);

        //完善员工信息
        employee.setUpdateTime(LocalDateTime.now()) //修改时间
                .setUpdateUser(JwtUserContext.getCurrentUserId()); //修改人

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
