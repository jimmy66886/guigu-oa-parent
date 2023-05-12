package com.zzmr.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzmr.model.system.SysMenu;
import com.zzmr.vo.system.AssginMenuVo;
import com.zzmr.vo.system.RouterVo;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author zzmr
 * @since 2023-05-07
 */
public interface SysMenuService extends IService<SysMenu> {

    // 菜单列表
    List<SysMenu> findNode();

    void removeMenuById(Long id);

    // 查询所有菜单和角色分配的菜单
    List<SysMenu> findMenuByRoleId(Long roleId);

    void doAssign(AssginMenuVo assginMenuVo);

    List<RouterVo> findUserMenuListByUserId(Long userId);

    List<String> findUserPermsByUserId(Long userId);
}
