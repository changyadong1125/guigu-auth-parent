package com.atguigu.springboot.service.imp;

import com.atguigu.springboot.mapper.SysRoleMapper;
import com.atguigu.springboot.mapper.SysUserRoleMapper;
import com.atguigu.springboot.model.system.SysRole;
import com.atguigu.springboot.model.system.SysUserRole;
import com.atguigu.springboot.model.vo.AssignRoleVo;
import com.atguigu.springboot.model.vo.SysRoleQueryVo;
import com.atguigu.springboot.service.SysRoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * project:PermissionManagement
 * package:com.atguigu.springboot.service
 * class:SysRoleServiceImp
 *
 * @author: smile
 * @create: 2023/4/26-18:28
 * @Version: v1.0
 * @Description:
 */
@Service
@Transactional
public class SysRoleServiceImp extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
    @Resource
    private SysUserRoleMapper sysUserRoleMapperImp;

    @Override
    public IPage<SysRole> selectPage(Page<SysRole> pageParam, SysRoleQueryVo roleQueryVo) {
        return baseMapper.selectPage(pageParam,roleQueryVo );
    }

    @Override
    public Map<String, Object> getRoleByUserId(Long id) {
        HashMap<String, Object> map = new HashMap<>();
        List<SysRole> sysRoles = baseMapper.selectList(null);
        map.put("allRoles",sysRoles);
        LambdaQueryWrapper<SysUserRole> sysUserRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserRoleLambdaQueryWrapper.eq(SysUserRole::getUserId,id);
        List<SysUserRole> sysUserRoles = sysUserRoleMapperImp.selectList(sysUserRoleLambdaQueryWrapper);
        List<Long> longList = sysUserRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
        map.put("userRoleIds",longList);
        return map;
    }

    @Override
    public void assignRole(AssignRoleVo assignRoleVo) {
        List<Long> roleIdList = assignRoleVo.getRoleIdList();
        Long userId = assignRoleVo.getUserId();
        //先把用户原来对应的用户id删除
        QueryWrapper<SysUserRole> sysUserRoleQueryWrapper = new QueryWrapper<>();
        sysUserRoleQueryWrapper.eq("user_id",userId);
        sysUserRoleMapperImp.delete(sysUserRoleQueryWrapper);
        //添加用户新的角色id
        for (Long aLong : roleIdList) {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(userId);
            sysUserRole.setRoleId(aLong);
            sysUserRoleMapperImp.insert(sysUserRole);
        }
    }
}
