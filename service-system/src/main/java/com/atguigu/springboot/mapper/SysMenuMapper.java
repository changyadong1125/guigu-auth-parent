package com.atguigu.springboot.mapper;

import com.atguigu.springboot.model.system.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * project:PermissionManagement
 * package:com.atguigu.springboot.mapper
 * class:SysMenuMapper
 *
 * @author: smile
 * @create: 2023/5/6-11:48
 * @Version: v1.0
 * @Description:
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    List<SysMenu> getUserMenuListById(Long id);
}
