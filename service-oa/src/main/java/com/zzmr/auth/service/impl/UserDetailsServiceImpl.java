package com.zzmr.auth.service.impl;

import com.zzmr.auth.service.SysMenuService;
import com.zzmr.auth.service.SysUserService;
import com.zzmr.model.system.SysUser;
import com.zzmr.security.custom.CustomUser;
import com.zzmr.security.custom.UserDetailsService;
import com.zzmr.vo.system.RouterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMenuService sysMenuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        SysUser sysUser = sysUserService.getUserByUserName(username);
        if (sysUser == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }

        if (sysUser.getStatus() == 0) {
            throw new RuntimeException("账号已停用");
        }

        // 根据用户id查询用户操作权限数据
        List<String> userPermsList = sysMenuService.findUserPermsByUserId(sysUser.getId());

        // 将集合封装为指定的泛型
        List<SimpleGrantedAuthority> authList = new ArrayList<>();
        // 查询出的list集合遍历
        for (String perms : userPermsList) {
            authList.add(new SimpleGrantedAuthority(perms.trim()));
        }

        return new CustomUser(sysUser, authList);
    }
}
