package com.senda.handler;

import com.senda.entity.Result;
import com.senda.exceptions.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public Result exceptionHandler(BaseException exception) {
        log.info("异常信息:{}", exception.getMessage());
        return Result.error(exception.getMessage());
    }
}