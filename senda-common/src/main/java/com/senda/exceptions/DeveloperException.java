package com.senda.exceptions;

/**
 * 开发者错误异常：用于捕捉代码设计、注解缺失、配置不当等开发阶段的问题。
 */
public class DeveloperException extends RuntimeException {

    public DeveloperException() {
    }

    public DeveloperException(String message) {
        super(message);
    }
}
