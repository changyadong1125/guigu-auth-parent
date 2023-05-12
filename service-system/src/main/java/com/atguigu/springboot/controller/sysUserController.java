package com.atguigu.springboot.controller;

import com.atguigu.springboot.common.result.Result;
import com.atguigu.springboot.common.util.MD5;
import com.atguigu.springboot.model.system.SysUser;
import com.atguigu.springboot.model.vo.SysUserQueryVo;
import com.atguigu.springboot.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * project:PermissionManagement
 * package:com.atguigu.springboot.controller
 * class:sysUserController
 *
 * @author: smile
 * @create: 2023/5/4-18:00
 * @Version: v1.0
 * @Description:
 */
@RestController
@Api(tags = "用户管理")
@RequestMapping("/admin/system/sysUser")
public class sysUserController {
    @Resource
    private SysUserService sysUserServiceImp;


    @GetMapping("/{pageNum}/{pageSize}")
    @ApiOperation("用户分页查询")
    @PreAuthorize("hasAuthority('bnt.sysUser.list')")
    public Result<?> pageList(
            @ApiParam("当前页") @PathVariable Integer pageNum,
            @ApiParam("每页条数") @PathVariable Integer pageSize,
            @ApiParam("查询条件") SysUserQueryVo sysUserQueryVo
    ) {
        Page<SysUser> sysUserPage = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SysUser> sysUserQueryWrapper = new LambdaQueryWrapper<>();

        if (!StringUtils.isEmpty(sysUserQueryVo.getKeyword())) {
            sysUserQueryWrapper.and(sysUserLambdaQueryWrapper -> sysUserLambdaQueryWrapper
                    .like(SysUser::getName, sysUserQueryVo.getKeyword()).or()
                    .like(SysUser::getUsername, sysUserQueryVo.getKeyword()).or()
                    .like(SysUser::getPhone, sysUserQueryVo.getKeyword()));
        }
        sysUserQueryWrapper.orderByDesc(SysUser::getCreateTime);
        sysUserQueryWrapper.like(!StringUtils.isEmpty(sysUserQueryVo.getKeyword()), SysUser::getName, sysUserQueryVo.getKeyword());
        sysUserQueryWrapper.ge(!StringUtils.isEmpty(sysUserQueryVo.getCreateTimeBegin()), SysUser::getCreateTime, sysUserQueryVo.getCreateTimeBegin());
        sysUserQueryWrapper.le(!StringUtils.isEmpty(sysUserQueryVo.getCreateTimeEnd()), SysUser::getCreateTime, sysUserQueryVo.getCreateTimeEnd());
        sysUserServiceImp.page(sysUserPage, sysUserQueryWrapper);
        return Result.ok(sysUserPage);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:增
     */
    @PostMapping("/update")
    @ApiOperation("用户新增")
    @PreAuthorize("hasAuthority('bnt.sysUser.add')")
    public Result<SysUser> save(@RequestBody SysUser sysUser) {
        sysUser.setPassword(MD5.encrypt(sysUser.getPassword()));
        sysUser.setStatus(1);
        sysUser.setHeadUrl("https://img.zcool.cn/community/016f5d5b84ef77a8012190f27a8c13.gif");
        sysUserServiceImp.save(sysUser);
        return Result.ok();
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:删
     */

    @DeleteMapping("/query/{id}")
    @ApiOperation("用户删除")
    @PreAuthorize("hasAuthority('bnt.sysUser.remove')")
    public Result<?> save(@PathVariable Long id) {
        sysUserServiceImp.removeById(id);
        return Result.ok();
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:改 回显
     */
    @ApiOperation("用户回显")
    @GetMapping("/update/{id}")
    @PreAuthorize("hasAuthority('bnt.sysUser.update')")
    public Result<SysUser> updateT(@PathVariable Long id) {
        SysUser sysUser = sysUserServiceImp.getById(id);
        return Result.ok(sysUser);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:改
     */
    @ApiOperation("用户修改")
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('bnt.sysUser.update')")
    public Result<?> update(@RequestBody SysUser sysUser) {
        sysUserServiceImp.updateById(sysUser);
        return Result.ok();
    }
    @ApiOperation("更改用户状态")
    @PutMapping("/update/{userId}/{status}")
    @PreAuthorize("hasAuthority('bnt.sysUser.update')")
    public Result<?> setStatus(@PathVariable Long userId,@PathVariable Integer status){
        if (sysUserServiceImp.updateStatus(userId, status)) return Result.ok();
        else return  Result.fail();
    }
}
