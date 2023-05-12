package com.atguigu.springboot.exception;

import com.atguigu.springboot.common.result.ResultCodeEnum;
import lombok.Data;


/**
 * project:PermissionManagement
 * package:com.atguigu.springboot.exception
 * class:MyException
 *
 * @author: smile
 * @create: 2023/4/27-7:52
 * @Version: v1.0
 * @Description: 自定义异常类
 */
@Data
@SuppressWarnings("all")
public class MyException extends RuntimeException{
    private Integer code;
    private String message;
    /**
     * return:
     * author: smile
     * version: 1.0
     * description:通过状态码和错误消息创建异常对象
     */
    public MyException(Integer code ,String message){
        super(message);
        this.code=code;
        this.message=message;
    }
    /**
     * return:
     * author: smile
     * version: 1.0
     * description:通过接收枚举类型对象创建异常对象
     */
    public MyException(ResultCodeEnum resultCodeEnum){
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
        this.message = resultCodeEnum.getMessage();
    }

}
