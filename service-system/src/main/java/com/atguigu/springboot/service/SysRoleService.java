package com.atguigu.springboot.service;

import com.atguigu.springboot.model.system.SysRole;
import com.atguigu.springboot.model.vo.AssignRoleVo;
import com.atguigu.springboot.model.vo.SysRoleQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * project:PermissionManagement
 * package:com.atguigu.springboot.service
 * class:SysRoleService
 *
 * @author: smile
 * @create: 2023/4/26-18:27
 * @Version: v1.0
 * @Description:
 */
public interface SysRoleService extends IService<SysRole> {
    IPage<SysRole> selectPage(Page<SysRole> pageParam, SysRoleQueryVo roleQueryVo);

    Map<String, Object> getRoleByUserId(Long id);

    void assignRole(AssignRoleVo assignRoleVo);
}
