package com.zzmr.auth.service.impl;

import com.zzmr.auth.mapper.SysUserMapper;
import com.zzmr.auth.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzmr.model.system.SysUser;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author zzmr
 * @since 2023-05-05
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    // 更新状态
    @Override
    public void updateStatus(Long id, Integer status) {
        SysUser sysUser = baseMapper.selectById(id);
        sysUser.setStatus(status);
        baseMapper.updateById(sysUser);
    }
}
