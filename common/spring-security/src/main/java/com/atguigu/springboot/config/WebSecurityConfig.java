package com.atguigu.springboot.config;

import com.atguigu.springboot.filter.TokenAuthenticationFilter;
import com.atguigu.springboot.filter.TokenLoginFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * project:PermissionManagement
 * package:com.atguigu.springboot.config
 * class:WebSecurityConfig
 *
 * @author: smile
 * @create: 2023/5/9-9:46
 * @Version: v1.0
 * @Description:
 */
@Configuration
@EnableWebSecurity //@EnableWebSecurity是开启SpringSecurity的默认行为
@EnableGlobalMethodSecurity(prePostEnabled = true)//开启注解功能，默认禁用注解
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //关闭跨站伪造攻击，一般https+UUID认证没有问题
        http.csrf().disable();
        //授权请求
        http.authorizeRequests()
                //.antMatchers("/admin/system/login").permitAll()
                .anyRequest().authenticated();
        http.addFilter(new TokenLoginFilter(redisTemplate, authenticationManager()));
        http.addFilterBefore(new TokenAuthenticationFilter(redisTemplate), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/favicon.ico", "/swagger-resources/**", "/webjars/**", "/v2/**", "/doc.html");
    }
}
