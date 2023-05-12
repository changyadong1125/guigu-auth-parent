package com.atguigu.springboot.filter;

import com.atguigu.springboot.common.result.Result;
import com.atguigu.springboot.common.util.ResponseUtil;
import com.atguigu.springboot.custom.CustomUser;
import com.atguigu.springboot.model.vo.LoginVo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * project:PermissionManagement
 * package:com.atguigu.springboot.filter
 * class:TokenLoginFilter
 *
 * @author: smile
 * @create: 2023/5/9-9:18
 * @Version: v1.0
 * @Description:
 */
public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:
     * 创建一个构造器 初始化RedisTemplate
     */
    public TokenLoginFilter(RedisTemplate<String, Object> redisTemplate, AuthenticationManager authenticationManager) {
        this.redisTemplate = redisTemplate;
        this.setAuthenticationManager(authenticationManager);
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/admin/system/login", "POST"));
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:
     * //用来代替@requestBody
     * UsernamePasswordAuthenticationToken authRequest;
     * try {
     * LoginVo loginVo = new ObjectMapper().readValue(request.getInputStream(), LoginVo.class);
     * authRequest = new UsernamePasswordAuthenticationToken(loginVo.getUsername(), loginVo.getPassword());
     * } catch (IOException e) {
     * throw new RuntimeException(e);
     * }
     * this.setDetails(request, authRequest);
     * return this.getAuthenticationManager().authenticate(authRequest);
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authRequest;
        try {
            LoginVo loginVo = new ObjectMapper().readValue(request.getInputStream(), LoginVo.class);
            authRequest = new UsernamePasswordAuthenticationToken(loginVo.getUsername(), loginVo.getPassword());
            return this.getAuthenticationManager().authenticate(authRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        //认证以后的对象 做强转
        CustomUser customUser = (CustomUser) authResult.getPrincipal();
        //返回前端一个map 放入用户的信息
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        redisTemplate.boundValueOps(token).set(customUser.getSysUser(), 2, TimeUnit.HOURS);
        //返回信息封装到map集合中
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("token", token);
        //通过流的方式返回给前端
        ResponseUtil.out(response, Result.ok(resMap));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        //通过流的方式返回给前端
        ResponseUtil.out(response, Result.build(null, 444, failed.getMessage()));
    }
}
