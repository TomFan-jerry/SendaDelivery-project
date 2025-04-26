package com.senda.mapper;

import com.senda.entity.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

    //员工登录
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(Employee employee);
}
