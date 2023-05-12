package com.atguigu.springboot.service.imp;

import com.atguigu.springboot.common.helper.MenuHelper;
import com.atguigu.springboot.mapper.SysMenuMapper;
import com.atguigu.springboot.mapper.SysRoleMenuMapper;
import com.atguigu.springboot.model.system.SysMenu;
import com.atguigu.springboot.model.system.SysRoleMenu;
import com.atguigu.springboot.model.vo.AssignMenuVo;
import com.atguigu.springboot.service.SysMenuService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;


/**
 * project:PermissionManagement
 * package:com.atguigu.springboot.service.imp
 * class:SysMenuServiceImp
 *
 * @author: smile
 * @create: 2023/5/6-11:47
 * @Version: v1.0
 * @Description:
 */
@Service
@Transactional
public class SysMenuServiceImp extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {
    @Resource
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public List<SysMenu> findMenuNodes() {
        List<SysMenu> sysMenuList = baseMapper.selectList(null);
        return MenuHelper.buildTree(sysMenuList);
    }

    @Override
    public List<SysMenu> getRoleMenuList(Long id) {

        //所有权限
        List<SysMenu> sysMenuList = baseMapper.selectList(null);
        //层级结构的权限列表
        List<SysMenu> sysMenus = MenuHelper.buildTree(sysMenuList);
        //查询用户权限
        List<SysRoleMenu> sysRoleMenus = sysRoleMenuMapper.selectList(new QueryWrapper<SysRoleMenu>().eq("role_id", id));
        List<Long> roleMenus = sysRoleMenus.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());
        for (SysMenu sysMenu : sysMenuList) {
            Long id1 = sysMenu.getId();
            if (roleMenus.contains(id1)) {
                sysMenu.setSelect(true);
            }
        }
        return sysMenus;
    }

    @Override
    public void assignMenu(AssignMenuVo assignMenuVo) {
        QueryWrapper<SysRoleMenu> sysRoleMenuQueryWrapper = new QueryWrapper<>();
        sysRoleMenuQueryWrapper.eq("role_id", assignMenuVo.getRoleId());
        sysRoleMenuMapper.delete(sysRoleMenuQueryWrapper);

        List<Long> menuIdList = assignMenuVo.getMenuIdList();
        for (Long aLong : menuIdList) {
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setRoleId(assignMenuVo.getRoleId());
            sysRoleMenu.setMenuId(aLong);
            sysRoleMenuMapper.insert(sysRoleMenu);
        }
    }
}
