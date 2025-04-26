package com.senda.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;

/**
 * 用户信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private Long id; // 主键
    private String openid; // 微信用户唯一标识
    private String name; // 姓名
    private String phone; // 手机号
    private String sex; // 性别
    private String idNumber; // 身份证号
    private String avatar; // 头像
    private LocalDateTime createTime;
}