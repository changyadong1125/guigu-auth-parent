package com.atguigu.springboot.controller;

import com.atguigu.springboot.common.result.Result;
import com.atguigu.springboot.model.vo.LoginVo;
import com.atguigu.springboot.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;


/**
 * project:PermissionManagement
 * package:com.atguigu.springboot
 * class:indexController
 *
 * @author: smile
 * @create: 2023/4/28-17:12
 * @Version: v1.0
 * @Description:
 */
@Api(tags = "后台登录管理")
@RestController
@RequestMapping("/admin/system")
public class LoginController {
    @Resource
    private SysUserService sysUserServiceImp;

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:
     * 该方法没有用到
     */
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody LoginVo loginVo) {
        Map<String, Object> ResMap = sysUserServiceImp.login(loginVo);
        return Result.ok(ResMap);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:
     * public Result<Map<String, Object>> info(HttpServletRequest httpServletRequest);
     * String token = httpServletRequest.getParameter("token");
     */
    @ApiOperation("登录信息")
    @GetMapping("/info")
    public Result<Map<String, Object>> info(@RequestHeader String token) {
        Map<String, Object> map = sysUserServiceImp.getUserInfoByToken(token);
        return Result.ok(map);
    }

    @ApiOperation("退出登录")
    @PostMapping("/logout")
    public Result<Map<String, Object>> logout(@RequestHeader String token) {
        sysUserServiceImp.logout(token);
        return Result.ok();
    }
}
