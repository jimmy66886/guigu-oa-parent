package com.zzmr.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zzmr.auth.mapper.SysMenuMapper;
import com.zzmr.auth.service.SysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzmr.auth.service.SysRoleMenuService;
import com.zzmr.auth.utils.MenuHelper;
import com.zzmr.common.config.exception.ZzmrException;
import com.zzmr.model.system.SysMenu;
import com.zzmr.model.system.SysRoleMenu;
import com.zzmr.vo.system.AssginMenuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author zzmr
 * @since 2023-05-07
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    // 菜单列表
    @Override
    public List<SysMenu> findNode() {

        // 1 查询所有菜单数据
        List<SysMenu> sysMenuList = baseMapper.selectList(null);
        // TODO 2 将这个全部菜单数据转换为有层级关系的结构--构建为树形结构
        List<SysMenu> resultList = MenuHelper.buildTree(sysMenuList);

        return resultList;
    }

    // 删除菜单-当一个菜单拥有子菜单,那就必须先删除子菜单,才能删除根菜单
    @Override
    public void removeMenuById(Long id) {
        // 判断当前菜单是否有子菜单
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMenu::getParentId, id);
        Integer count = baseMapper.selectCount(wrapper);
        if (count > 0) {
            throw new ZzmrException(201, "菜单不能删除");
        } else {
            baseMapper.deleteById(id);
        }
    }

    // 查询该角色拥有的菜单
    @Override
    public List<SysMenu> findMenuByRoleId(Long roleId) {
        // 1 查询所有的菜单 状态为1的(表示可用)
        LambdaQueryWrapper<SysMenu> wrapperSysMenu = new LambdaQueryWrapper<>();
        wrapperSysMenu.eq(SysMenu::getStatus, 1);
        List<SysMenu> allSysMenuList = baseMapper.selectList(wrapperSysMenu);
        // 2 根据角色roleId查询  sys_role_menu 查到角色id对应的所有的菜单id
        LambdaQueryWrapper<SysRoleMenu> wrapperRoleMenu = new LambdaQueryWrapper<>();
        wrapperRoleMenu.eq(SysRoleMenu::getRoleId, roleId);
        List<SysRoleMenu> sysRoleMenuList = sysRoleMenuService.list(wrapperRoleMenu);
        // 3 根据获取菜单id,获取对应菜单对象
        List<Long> menuIdList = sysRoleMenuList.stream().map(c -> c.getMenuId()).collect(Collectors.toList());
        // 3.1 拿着菜单id 和所有菜单集合里面的id进行比较 ,如果相同就封装
        allSysMenuList.stream().forEach(item -> {
            if (menuIdList.contains(item.getId())) {
                item.setSelect(true);
            } else {
                item.setSelect(false);
            }
        });
        // 4 返回规定树形格式菜单列表
        List<SysMenu> sysMenuList = MenuHelper.buildTree(allSysMenuList);
        return sysMenuList;
    }

    // 为角色分配菜单
    @Override
    public void doAssign(AssginMenuVo assginMenuVo) {
        // 根据角色id删除表中已经分配的菜单
        LambdaQueryWrapper<SysRoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRoleMenu::getRoleId, assginMenuVo.getRoleId());
        sysRoleMenuService.remove(wrapper);
        // 再插入
        List<Long> menuIdList = assginMenuVo.getMenuIdList();
        for (Long menuId : menuIdList) {
            if (StringUtils.isEmpty(menuId)) {
                continue;
            }
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setMenuId(menuId);
            sysRoleMenu.setRoleId(assginMenuVo.getRoleId());
            sysRoleMenuService.save(sysRoleMenu);
        }
    }
}
