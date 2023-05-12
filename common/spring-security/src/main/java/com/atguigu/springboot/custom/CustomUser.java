package com.atguigu.springboot.custom;


import com.atguigu.springboot.model.system.SysUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * project:PermissionManagement
 * package:com.atguigu.springboot.custom
 * class:CustomUser
 *
 * @author: smile
 * @create: 2023/5/8-16:39
 * @Version: v1.0
 * @Description:
 */
public class CustomUser extends User {
    private SysUser sysUser;

    public CustomUser(SysUser sysUser, Collection<? extends GrantedAuthority> authorities) {
        super(sysUser.getUsername(), sysUser.getPassword(), authorities);
        this.sysUser = sysUser;
    }

    public SysUser getSysUser() {
        return sysUser;
    }

    public void setSysUser(SysUser sysUser) {
        this.sysUser = sysUser;
    }

}
