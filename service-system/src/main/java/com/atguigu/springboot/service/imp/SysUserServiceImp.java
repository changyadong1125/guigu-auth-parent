package com.atguigu.springboot.service.imp;

import com.atguigu.springboot.common.helper.MenuHelper;
import com.atguigu.springboot.common.helper.RouterHelper;
import com.atguigu.springboot.common.result.ResultCodeEnum;
import com.atguigu.springboot.common.util.MD5;
import com.atguigu.springboot.exception.MyException;
import com.atguigu.springboot.mapper.SysMenuMapper;
import com.atguigu.springboot.mapper.SysUserMapper;
import com.atguigu.springboot.model.system.SysMenu;
import com.atguigu.springboot.model.system.SysUser;
import com.atguigu.springboot.model.vo.LoginVo;
import com.atguigu.springboot.model.vo.RouterVo;
import com.atguigu.springboot.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * project:PermissionManagement
 * package:com.atguigu.springboot.service.imp
 * class:SysUserServiceImp
 *
 * @author: smile
 * @create: 2023/4/29-23:52
 * @Version: v1.0
 * @Description:
 */
@Service
@Transactional
public class SysUserServiceImp extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Resource
    private RedisTemplate<Object, Object> redisTemplate;
    @Resource
    private SysMenuMapper sysMenuMapper;

    public boolean updateStatus(Long id, Integer status) {
        SysUser sysUser = new SysUser();
        sysUser.setStatus(status);
        UpdateWrapper<SysUser> sysUserUpdateWrapper = new UpdateWrapper<>();
        sysUserUpdateWrapper.eq("id", id);
        int update = baseMapper.update(sysUser, sysUserUpdateWrapper);
        return update > 0;
    }

    public ArrayList<String> getUserButtonsByUserId(List<SysMenu> userMenus) {
        ArrayList<String> permsList = new ArrayList<>();
        for (SysMenu userMenu : userMenus) {
            if (userMenu.getType() == 2) {
                String perms = userMenu.getPerms();
                permsList.add(perms);
            }
        }
        return permsList;
    }

    @Override
    public SysUser getUserInfoByUsername(String username) {
        LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserLambdaQueryWrapper.eq(SysUser::getUsername, username);
        return baseMapper.selectOne(sysUserLambdaQueryWrapper);
    }

    @Override
    public Map<String, Object> getUserInfoByToken(String token) {
        HashMap<String, Object> map = new HashMap<>();
        SysUser sysUser = (SysUser) redisTemplate.boundValueOps(token).get();
        System.out.println(sysUser);
        assert sysUser != null;
        //获取当前用户的权限列表
        List<SysMenu> userMenus = getUserMenusByUserId(sysUser);
        //转换为有层级结构的权限列表
        List<SysMenu> sysMenuList = MenuHelper.buildTree(userMenus);
        //转换为有层级结构的路由列表
        List<RouterVo> routerVos = RouterHelper.buildRouters(sysMenuList);
        //获取当前用户的按钮的权限列表
        ArrayList<String> permsList = getUserButtonsByUserId(userMenus);

        ArrayList<String> strings = new ArrayList<>();
        strings.add("admin");
        map.put("roles", strings);
        map.put("introduction", " I am a nice person");
        map.put("avatar", sysUser.getHeadUrl());
        map.put("name", sysUser.getUsername());
        map.put("routers", routerVos);
        map.put("buttons", permsList);
        return map;
    }

    public List<SysMenu> getUserMenusByUserId(SysUser sysUser) {
        Long id = sysUser.getId();
        List<SysMenu> sysMenuList;
        //判断该用户是不是超级管理员
        if (id == 1) {
            QueryWrapper<SysMenu> sysMenuQueryWrapper = new QueryWrapper<>();
            sysMenuQueryWrapper.eq("status", 1);
            sysMenuQueryWrapper.orderByAsc("sort_value");
            sysMenuList = sysMenuMapper.selectList(sysMenuQueryWrapper);
        } else {
            sysMenuList = sysMenuMapper.getUserMenuListById(id);
        }
        return sysMenuList;
    }

    @Override
    public Map<String, Object> login(LoginVo loginVo) {
        String password = loginVo.getPassword();
        String username = loginVo.getUsername();
        if (StringUtils.isEmpty(password) || StringUtils.isEmpty(username)) {
            throw new MyException(ResultCodeEnum.ILLEGAL_REQUEST);
        }
        //根据用户名查询用户信息
        SysUser sysUser = getUserInfoByUsername(username);
        if (sysUser == null) {
            throw new MyException(ResultCodeEnum.ACCOUNT_ERROR);
        }
        if (!MD5.encrypt(password).equals(sysUser.getPassword())) {
            throw new MyException(ResultCodeEnum.PASSWORD_ERROR);
        }
        if (sysUser.getStatus() == 0) {
            throw new MyException(ResultCodeEnum.ACCOUNT_STOP);
        }

        String token = UUID.randomUUID().toString().replaceAll("-", "");
        redisTemplate.boundValueOps(token).set(sysUser, 2, TimeUnit.HOURS);

        Map<String, Object> resMap = new HashMap<>();
        resMap.put("token", token);
        return resMap;
    }

    @Override
    public void logout(String token) {
        redisTemplate.delete(token);
    }
}
