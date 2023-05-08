package com.zzmr.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzmr.model.system.SysUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author zzmr
 * @since 2023-05-05
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

}
