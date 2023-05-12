package com.atguigu.springboot.service;

import com.atguigu.springboot.model.system.SysMenu;
import com.atguigu.springboot.model.system.SysUser;
import com.atguigu.springboot.model.vo.LoginVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * project:PermissionManagement
 * package:com.atguigu.springboot.service
 * class:SysUserImp
 *
 * @author: smile
 * @create: 2023/4/29-23:48
 * @Version: v1.0
 * @Description:
 */
public interface SysUserService extends IService<SysUser> {
    boolean updateStatus(Long id, Integer status);

    SysUser getUserInfoByUsername(String username);

    Map<String, Object> getUserInfoByToken(String token);

    Map<String, Object> login(LoginVo loginVo);

    List<SysMenu> getUserMenusByUserId(SysUser sysUser);

    ArrayList<String> getUserButtonsByUserId(List<SysMenu> userMenus);

    void logout(String token);
}
