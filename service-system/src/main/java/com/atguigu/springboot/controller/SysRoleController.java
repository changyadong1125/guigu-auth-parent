package com.atguigu.springboot.controller;

import com.atguigu.springboot.common.result.Result;
import com.atguigu.springboot.model.system.SysRole;
import com.atguigu.springboot.model.vo.AssignRoleVo;
import com.atguigu.springboot.model.vo.SysRoleQueryVo;
import com.atguigu.springboot.service.SysRoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * project:PermissionManagement
 * package:com.atguigu.springboot.controller
 * class:SysRoleController
 *
 * @author: smile
 * @create: 2023/4/26-19:04
 * @Version: v1.0
 * @Description:
 */
@Api(tags = "角色管理")
@RestController
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {
    @Resource
    private SysRoleService sysRoleServiceImp;



    /**
     * return:
     * author: smile
     * version: 1.0
     * description: 分页
     * required 参数是否是必须的 true为必须的 默认为true
     */
    @ApiOperation(value = "获取分页")
    @PostMapping("/{page}/{limit}")
    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    public Result<?> index(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Integer page,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Integer limit,
            @ApiParam(name = "roleQueryVo", value = "查询对象", required = true) @RequestBody SysRoleQueryVo sysRoleQueryVo) {
        Page<SysRole> pageParam = new Page<>(page, limit);
        IPage<SysRole> pageModel = sysRoleServiceImp.selectPage(pageParam, sysRoleQueryVo);
        return Result.ok(pageModel);
    }


    /**
     * return:
     * author: smile
     * version: 1.0
     * description:通过通用service查询分页信息
     */
    @ApiOperation("角色分页查询")
    @PostMapping("/pageBy/{pageNum}/{pageSize}")
    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    public Result<?> page(@PathVariable Integer pageNum,
                       @PathVariable Integer pageSize,
                       @RequestBody  SysRoleQueryVo sysRoleQueryVo){
        Page<SysRole> page=new Page<>(pageNum,pageSize);
        LambdaQueryWrapper<SysRole> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(sysRoleQueryVo.getRoleName()),SysRole::getRoleName,sysRoleQueryVo.getRoleName());
        sysRoleServiceImp.page(page,queryWrapper);
        return Result.ok(page);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:增
     */
    @ApiOperation("角色新增")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('bnt.sysRole.add')")
    public Result<SysRole> save(@RequestBody SysRole sysRole){
        sysRoleServiceImp.save(sysRole);
        return Result.ok();
    }
    /**
     * return:
     * author: smile
     * version: 1.0
     * description:删除单个
     */

    @ApiOperation("角色删除")
    @DeleteMapping("/query/{id}")
    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    public Result<SysRole> deleteOne(@PathVariable Long id){
        sysRoleServiceImp.removeById(id);
        return Result.ok();
    }
    /**
     * return:
     * author: smile
     * version: 1.0
     * description:批量删除
     */
    @ApiOperation("角色批量删除")
    @DeleteMapping("/query")
    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    public Result<SysRole> deleteBatch(@RequestBody List<Long> idList){
        sysRoleServiceImp.removeByIds(idList);
        return Result.ok();
    }
    /**
     * return:
     * author: smile
     * version: 1.0
     * description:角色回显
     */
    @ApiOperation("角色回显")
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('bnt.sysRole.update')")
    public Result<SysRole> update(@PathVariable Long id){
        SysRole sysRole = sysRoleServiceImp.getById(id);
        return Result.ok(sysRole);
    }
    /**
     * return:
     * author: smile
     * version: 1.0
     * description:修改
     */
    @ApiOperation("角色修改")
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('bnt.sysRole.update')")
    public Result<SysRole> edit(@RequestBody SysRole sysRole){
        sysRole.setUpdateTime(new Date());
        sysRoleServiceImp.updateById(sysRole);
        return Result.ok();
    }
    /**
     * return:
     * author: smile
     * version: 1.0
     * description:获取所有的角色列表和当前用户的角色
     */
    @ApiOperation(value = "角色查询")
    @GetMapping("/query/{id}")
    @PreAuthorize("hasAuthority('bnt.sysUser.assignRole')")
    public Result<?> get(@PathVariable Long id){
        Map<String,Object> map = sysRoleServiceImp.getRoleByUserId(id);
        return Result.ok(map);
    }
    /**
     * return:
     * author: smile
     * version: 1.0
     * description:给用户绑定角色
     */
    @PostMapping("/doAssign")
    @PreAuthorize("hasAuthority('bnt.sysUser.assignRole')")
    public Result<?> doAssign(@RequestBody AssignRoleVo assignRoleVo){
        sysRoleServiceImp.assignRole(assignRoleVo);
        return Result.ok();
    }
}
