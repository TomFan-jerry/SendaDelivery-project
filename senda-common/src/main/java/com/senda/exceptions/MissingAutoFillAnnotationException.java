package com.senda.exceptions;

/**
 * 表示方法缺少 @AutoFill 注解的异常
 */
public class MissingAutoFillAnnotationException extends DeveloperException {

    public MissingAutoFillAnnotationException() {
    }

    public MissingAutoFillAnnotationException(String message) {
        super(message);
    }
}
