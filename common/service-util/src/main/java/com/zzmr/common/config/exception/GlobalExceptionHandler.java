package com.zzmr.common.config.exception;

import com.zzmr.common.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 全局异常处理,执行的方法
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e) {
        e.printStackTrace();
        return Result.fail().message("执行了全局异常处理....");
    }

    // 特定异常处理
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public Result error(ArithmeticException e) {
        e.printStackTrace();
        return Result.fail().message("执行了特定异常处理....");
    }

    // 自定义异常处理
    @ExceptionHandler(ZzmrException.class)
    @ResponseBody
    public Result error(ZzmrException e) {
        e.printStackTrace();
        return Result.fail().code(e.getCode()).message(e.getMsg());
    }

}
