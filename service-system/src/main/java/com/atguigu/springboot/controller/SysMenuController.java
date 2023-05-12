package com.atguigu.springboot.controller;


import com.atguigu.springboot.common.result.Result;
import com.atguigu.springboot.model.system.SysMenu;
import com.atguigu.springboot.model.vo.AssignMenuVo;
import com.atguigu.springboot.service.SysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * project:PermissionManagement
 * package:com.atguigu.springboot.controller
 * class:SysMenuController
 *
 * @author: smile
 * @create: 2023/5/6-11:51
 * @Version: v1.0
 * @Description:
 */
@Api(tags = "菜单管理")
@RestController
@RequestMapping("/admin/system/sysMenu")
public class SysMenuController {
    @Resource
    private SysMenuService sysMenuServiceImp;

    @ApiOperation("查询菜单")
    @GetMapping("/findMenuNodes")
    @PreAuthorize("hasAuthority('bnt.sysMenu.list')")
    public Result<?> findMenuNodes() {
        List<SysMenu> sysMenuList = sysMenuServiceImp.findMenuNodes();
        return Result.ok(sysMenuList);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:
     */
    @ApiOperation("菜单添加")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('bnt.sysMenu.add')")
    public Result<?> save(@RequestBody SysMenu sysMenu) {
        sysMenuServiceImp.save(sysMenu);
        return Result.ok();
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    @ApiOperation("菜单删除")
    @DeleteMapping("/delete/{id}")
    public Result<?> delete(@PathVariable Long id) {
        sysMenuServiceImp.removeById(id);
        return Result.ok();
    }

    @ApiOperation("菜单回显")
    @GetMapping("/update/{id}")
    @PreAuthorize("hasAuthority('bnt.sysMenu.list')")
    public Result<?> edit(@PathVariable Long id) {
        SysMenu sysMenu = sysMenuServiceImp.getById(id);
        return Result.ok(sysMenu);
    }

    @ApiOperation("菜单修改")
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('bnt.sysMenu.update')")
    public Result<?> edit(@RequestBody SysMenu sysMenu) {
        sysMenu.setUpdateTime(null);
        sysMenuServiceImp.updateById(sysMenu);
        return Result.ok();
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:回显所有权限列表和当前角色的权限
     */
    @ApiOperation("权限分配页面")
    @GetMapping("/getRoleMenuList/{id}")
    @PreAuthorize("hasAuthority('bnt.sysRole.assignAuth')")
    public Result<?> getRoleMenuList(@PathVariable Long id) {
        List<SysMenu> list = sysMenuServiceImp.getRoleMenuList(id);
        return Result.ok(list);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:给当前角色分配新的权限
     */
    @ApiOperation("权限分配")
    @PostMapping("/assignMenu")
    @PreAuthorize("hasAuthority('bnt.sysRole.assignAuth')")
    public Result<?> getRoleMenuList(@RequestBody AssignMenuVo assignMenuVo) {
        sysMenuServiceImp.assignMenu(assignMenuVo);
        return Result.ok();
    }

}
