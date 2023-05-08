package com.zzmr.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzmr.auth.mapper.SysRoleMapper;
import com.zzmr.auth.service.SysRoleService;
import com.zzmr.auth.service.SysUserRoleService;
import com.zzmr.model.system.SysRole;
import com.zzmr.model.system.SysUserRole;
import com.zzmr.vo.system.AssginRoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired
    private SysUserRoleService sysUserRoleService;

    // 1 查询所有角色 和 当前用户所属角色
    @Override
    public Map<String, Object> findRoleDataByUserId(Long userId) {

        // 1 查询所有的角色,返回List集合   返回
        List<SysRole> allRolesList = baseMapper.selectList(null);
        // 2 根据userId查询角色用户关系表,查询userId对应的所有角色id
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, userId);
        // 该用户对应的所有的角色
        List<SysUserRole> existUserRoleList = sysUserRoleService.list(wrapper);
        // 从查询出的泛型为SysUserRole的List集合中取出roleId

        /* 普通方式
        List<Long> existRoleIdList = new ArrayList<>();
        for (SysUserRole sysUserRole : existUserRoleList) {
            Long roleId = sysUserRole.getRoleId();
            existRoleIdList.add(roleId);
        }*/
        // stream流的方式
        List<Long> existRoleIdList = existUserRoleList.stream().map(c -> c.getRoleId()).collect(Collectors.toList());

        // 3 根据查询所有的角色id,找到对应角色信息   根据角色id和所有的角色的List集合进行比较
        List<SysRole> assginRoleList = new ArrayList<>();
        for (SysRole sysRole : allRolesList) {
            // 比较 某用户所对应的所有角色id,是否和全部角色id有相同的,有,则将该角色放到assignRoleList中
            if (existRoleIdList.contains(sysRole.getId())) {
                assginRoleList.add(sysRole);
            }
        }

        // 4 得到两部分数据封装到map集合,返回
        Map<String, Object> roleMap = new HashMap<>();
        roleMap.put("assginRoleList", assginRoleList);
        roleMap.put("allRolesList", allRolesList);
        return roleMap;
    }

    // 2 为用户分配角色
    @Override
    public void doAssign(AssginRoleVo assginRoleVo) {
        // 把用户之前分配角色数据删除

        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        // 根据userId在UserRole表中删除所有已分配的信息
        wrapper.eq(SysUserRole::getUserId, assginRoleVo.getUserId());
        sysUserRoleService.remove(wrapper);
        // 重新进行分配   所有角色id的List  和 userId
        // 角色id的List
        List<Long> roleIdList = assginRoleVo.getRoleIdList();
        // 用户的id
        Long userId = assginRoleVo.getUserId();
        for (Long roleId : roleIdList) {
            if (StringUtils.isEmpty(roleId)) {
                continue;
            }
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setRoleId(roleId);
            sysUserRole.setUserId(userId);
            sysUserRoleService.save(sysUserRole);
        }
    }
}
