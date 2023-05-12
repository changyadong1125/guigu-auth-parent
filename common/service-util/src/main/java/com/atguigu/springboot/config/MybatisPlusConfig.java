package com.atguigu.springboot.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * project:PermissionManagement
 * package:com.atguigu.springboot.config
 * class:MybatisPlusConfig
 *
 * @author: smile
 * @create: 2023/4/26-20:08
 * @Version: v1.0
 * @Description:
 */
@EnableTransactionManagement
@Configuration
@MapperScan(basePackages = "com.atguigu.springboot.mapper")
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor addPaginationInnerInterceptor(){
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        //向Mybatis过滤器链中添加分页拦截器
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
