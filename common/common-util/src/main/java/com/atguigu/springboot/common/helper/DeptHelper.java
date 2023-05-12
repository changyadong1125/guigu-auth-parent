package com.atguigu.springboot.common.helper;


import com.atguigu.springboot.model.system.SysDept;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 根据权限数据构建菜单数据
 * </p>
 */
@SuppressWarnings("all")
public class DeptHelper {

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:把一个List转成树
     */
    public static List<SysDept> buildTree(List<SysDept> list, Long parentId) {
        List<SysDept> tree = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            for (SysDept org : list) {
                if (Objects.equals(org.getParentId(), parentId)) {
                    tree.add(findChild(org, list));
                }
            }
        }
        return tree;
    }

    private static SysDept findChild(SysDept org, List<SysDept> list) {
        for (SysDept n : list) {
            if (Objects.equals(n.getParentId(), org.getId())) {
                if (org.getChildren() == null) {
                    org.setChildren(new ArrayList<SysDept>());
                }
                org.getChildren().add(findChild(n, list));
            }
        }
        return org;
    }
}
