package com.zzmr.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzmr.model.system.SysUser;
import io.swagger.models.auth.In;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author zzmr
 * @since 2023-05-05
 */
public interface SysUserService extends IService<SysUser> {

    void updateStatus(Long id, Integer status);

    SysUser getUserByUserName(String username);
}
