package com.atguigu.springboot.handler;

import com.atguigu.springboot.common.result.Result;
import com.atguigu.springboot.common.result.ResultCodeEnum;
import com.atguigu.springboot.exception.MyException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * return:
 * author: smile
 * version: 1.0
 * description:创建全局异常处理类
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Result<?> error(Exception e) {
        e.printStackTrace();
        return Result.fail();
    }

    @ExceptionHandler(ArithmeticException.class)
    public Result<?> error(ArithmeticException e) {
        e.printStackTrace();
        return Result.fail().message("数学异常");

    }

    @ExceptionHandler(MyException.class)
    public Result<?> error(MyException e) {
        e.printStackTrace();
        return Result.fail().message(e.getMessage()).code(e.getCode());
    }

    @ExceptionHandler(RuntimeException.class)
    public Result<?> error(RuntimeException e) {
        e.printStackTrace();
        return Result.fail().message(e.getMessage());

    }

    @ExceptionHandler(AccessDeniedException.class)
    public Result<?> error(AccessDeniedException e) {
        e.printStackTrace();
        return Result.build(null, ResultCodeEnum.PERMISSION);

    }
}
