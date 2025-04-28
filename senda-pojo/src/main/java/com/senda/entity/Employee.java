package com.senda.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 员工信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@Component
public class Employee {

    private Long id; // 主键
    private String name; // 姓名
    private String username; // 用户名
    private String password; // 密码
    private String phone; // 手机号
    private String sex; // 性别
    private String idNumber; // 身份证号
    private Integer status; // 状态 0:禁用，1:启用
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间
    private Long createUser; // 创建人
    private Long updateUser; // 修改人
}