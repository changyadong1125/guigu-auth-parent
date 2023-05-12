package com.atguigu.springboot.mapper;



import com.atguigu.springboot.model.system.SysRole;
import com.atguigu.springboot.model.vo.SysRoleQueryVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * project:PermissionManagement
 * package:com.atguigu.springboot.mapper
 * class:SysRoleMapper
 *
 * @author: smile
 * @create: 2023/4/26-16:27
 * @Version: v1.0
 * @Description:
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {
    IPage<SysRole> selectPage(Page<SysRole> page, @Param("vo") SysRoleQueryVo roleQueryVo);

    List<SysRole> selectList(Object o);
}
