package com.atguigu.springboot.service;

import com.atguigu.springboot.model.system.SysMenu;
import com.atguigu.springboot.model.vo.AssignMenuVo;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * project:PermissionManagement
 * package:com.atguigu.springboot.service
 * class:SysMenuService
 *
 * @author: smile
 * @create: 2023/5/6-11:46
 * @Version: v1.0
 * @Description:
 */
public interface SysMenuService extends IService<SysMenu> {
    List<SysMenu> findMenuNodes();

    List<SysMenu> getRoleMenuList(Long id);

    void assignMenu(AssignMenuVo assignMenuVoVo);
}
