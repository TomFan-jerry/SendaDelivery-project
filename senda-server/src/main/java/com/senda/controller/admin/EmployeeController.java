package com.senda.controller.admin;

import com.senda.entity.Employee;
import com.senda.entity.Result;
import com.senda.service.EmployeeService;
import com.senda.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/admin/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    //员工登录
    @PostMapping("/login")
    public Result login(@RequestBody Employee employee) {
        log.info("员工登录:{}", employee);
        Employee emp = employeeService.login(employee);

        if (emp != null) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", emp.getId());
            claims.put("name", emp.getName());
            claims.put("username", emp.getUsername());
            String jwt = JwtUtils.generateToken(claims);

            Map<String, Object> data = new LinkedHashMap<>();
            data.put("id", emp.getId());
            data.put("username", emp.getUsername());
            data.put("name", emp.getName());
            data.put("token", jwt);

            return Result.success(data);
        }

        return Result.error("用户名或密码错误");
    }

    //员工登出
    @PostMapping("/logout")
    public Result logout() {
        log.info("员工登出");
        return Result.success();
    }

}
