package com.atguigu.springboot.common.helper;


import com.atguigu.springboot.model.system.SysMenu;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


/**
 * 根据菜单数据构建菜单树的工具类
 */
@SuppressWarnings("all")
public class MenuHelper {

    /**
     * 使用递归方法建菜单
     */

    public static List<SysMenu> buildTree(List<SysMenu> sysMenuList) {
        List<SysMenu> trees = new ArrayList<>();
        if (!CollectionUtils.isEmpty(sysMenuList)) {
            for (SysMenu sysMenu : sysMenuList) {
                if (sysMenu.getParentId().longValue() == 0) {
                    trees.add(findChildren(sysMenu, sysMenuList));
                }
            }
        }
        return trees;
    }

    /**
     * 递归查找子节点
     *
     * @param treeNodes
     * @return
     */
    public static SysMenu findChildren(SysMenu sysMenu, List<SysMenu> treeNodes) {
        sysMenu.setChildren(new ArrayList<SysMenu>());
        for (SysMenu it : treeNodes) {
            if (sysMenu.getId().longValue() == it.getParentId().longValue()) {
                if (sysMenu.getChildren() == null) {
                    sysMenu.setChildren(new ArrayList<>());
                }
                sysMenu.getChildren().add(findChildren(it, treeNodes));
            }
        }
        return sysMenu;
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:
     * 与递归算法不同，BFS 算法采用队列来保存待遍历的节点。我们首先创建一个空队列，并将 sysMenu 加入队列中。
     * <p>
     * 当队列不为空时，我们从队列中取出每个节点，并遍历整个树形结构，查找该节点的所有子节点。如果某个节点是当前节点的子节点，则将其添加到当前节点的 children 属性中，
     * 并将该节点加入队列中，以便后续对其进行处理。
     * <p>
     * 通过循环遍历队列，我们最终可以将 sysMenu 的所有子节点全部找到，并存储在 sysMenu 对象的 children 属性中。最后，我们返回 sysMenu 对象即可。
     * <p>
     * 由于 BFS 算法始终保持了队列的大小不超过树形结构的节点数，因此可以有效地避免递归算法中可能出现的内存问题，并且能够更快速地找到所有子节点。
     */
    public static SysMenu findChildrenBFS(SysMenu sysMenu, List<SysMenu> treeNodes) {
        sysMenu.setChildren(new ArrayList<>());
        Queue<SysMenu> queue = new LinkedList<>();
        queue.offer(sysMenu);
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                SysMenu node = queue.poll();
                for (SysMenu it : treeNodes) {
                    if (node.getId().equals(it.getParentId())) {
                        if (node.getChildren() == null) {
                            node.setChildren(new ArrayList<>());
                        }
                        node.getChildren().add(it);
                        queue.offer(it);
                    }
                }
            }
        }
        return sysMenu;
    }
}