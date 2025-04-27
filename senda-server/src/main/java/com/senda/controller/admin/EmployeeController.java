package com.senda.controller.admin;

import com.senda.dto.EmployeeDTO;
import com.senda.dto.EmployeeLoginDTO;
import com.senda.dto.EmployeePageQueryDTO;
import com.senda.entity.Employee;
import com.senda.result.PageResult;
import com.senda.result.Result;
import com.senda.service.EmployeeService;
import com.senda.utils.JwtUtils;
import com.senda.vo.EmployeeLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/admin/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

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
        return Result.success();
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
        return Result.success();
    }

    /**
     * 员工分页查询
     * @param name
     * @param page
     * @param pageSize
     * @return
     */
    //TODO 优化数据传输
    @GetMapping("/page")
    public Result<PageResult<Employee>> employeePage(EmployeePageQueryDTO employeePageQueryDTO) {
        PageResult<Employee> pageResult = employeeService.employeePage(employeePageQueryDTO);
        return Result.success(null, pageResult);
    }

}
