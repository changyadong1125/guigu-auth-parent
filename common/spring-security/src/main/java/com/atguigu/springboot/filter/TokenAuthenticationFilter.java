package com.atguigu.springboot.filter;

import com.atguigu.springboot.common.result.Result;
import com.atguigu.springboot.common.result.ResultCodeEnum;
import com.atguigu.springboot.common.util.ResponseUtil;
import com.atguigu.springboot.model.system.SysUser;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * project:PermissionManagement
 * package:com.atguigu.springboot.filter
 * class:TokenAuthenticationFilter
 *
 * @author: smile
 * @create: 2023/5/9-10:52
 * @Version: v1.0
 * @Description: 做token解析的
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final RedisTemplate<String, Object> redisTemplate;

    public TokenAuthenticationFilter(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = httpServletRequest.getRequestURI();
        //如果是的登录请求就放行
        if ("/admin/system/login".equals(requestURI)) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        //获取认证后的用户信息
        Authentication authentication = GetAuthenticationInfo(httpServletRequest);
        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } else {
            ResponseUtil.out(httpServletResponse, Result.build(null, ResultCodeEnum.PERMISSION));
        }
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:
     * ArrayList<SimpleGrantedAuthority> simpleGrantedAuthorityArrayList = new ArrayList<>();
     * for (String s : userPermsList) {
     * SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(s);
     * simpleGrantedAuthorityArrayList.add(simpleGrantedAuthority);
     * }
     */
    private Authentication GetAuthenticationInfo(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("token");
        //获取用户信息
        if (!StringUtils.isEmpty(token)) {
            SysUser sysUser = (SysUser) redisTemplate.boundValueOps(token).get();
            if (sysUser != null) {
                if (sysUser.getUserPermsList() != null && sysUser.getUserPermsList().size() > 0) {
                    List<String> userPermsList = sysUser.getUserPermsList();
                    List<SimpleGrantedAuthority> simpleGrantedAuthorityList = userPermsList
                            .stream().filter(perm -> !StringUtils.isEmpty(perm))
                            .map(SimpleGrantedAuthority::new).collect(Collectors.toList());
                    return new UsernamePasswordAuthenticationToken(sysUser, sysUser.getPassword(), simpleGrantedAuthorityList);
                } else {
                    return new UsernamePasswordAuthenticationToken(sysUser, sysUser.getPassword(), Collections.emptyList());
                }
            }
        }
        return null;
    }
}
