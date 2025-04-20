package com.chen.picture.exception;

import com.chen.picture.common.BaseResponse;
import com.chen.picture.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice //标记此类为全局异常处理器
@Slf4j //日志
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class) //只要有controller发生了指定的异常，就会被这个方法处理。
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        log.error("BusinessException", e);
        return ResultUtils.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("RuntimeException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "系统错误");
    }
}