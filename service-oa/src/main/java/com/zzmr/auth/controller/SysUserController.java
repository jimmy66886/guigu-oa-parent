package com.zzmr.auth.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzmr.auth.service.SysUserService;
import com.zzmr.common.result.Result;
import com.zzmr.common.utils.MD5;
import com.zzmr.model.system.SysUser;
import com.zzmr.vo.system.SysUserQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author zzmr
 * @since 2023-05-05
 */
@RestController
@RequestMapping("/admin/system/sysUser")
@Api(tags = "用户管理接口")
public class SysUserController {
    @Autowired
    private SysUserService service;

    // 更改用户状态
    @ApiOperation("更改用户状态")
    @GetMapping("updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable Long id, @PathVariable Integer status) {
        service.updateStatus(id, status);
        return Result.ok();
    }

    // 用户条件分页查询
    @ApiOperation("用户条件分页查询")
    @GetMapping("/{page}/{limit}")
    public Result pageQueryUser(@PathVariable Long page, @PathVariable Long limit, SysUserQueryVo sysUserQueryVo) {
        // 创建page对象,传递分页相关参数
        Page<SysUser> pageParam = new Page<>(page, limit);

        // 封装条件
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        String username = sysUserQueryVo.getKeyword();  // 用户名
        // 下面两个时间,相当于管理员会选择一个时间范围,然后筛选出该范围内创建的用户
        String createTimeBegin = sysUserQueryVo.getCreateTimeBegin();
        String createTimeEnd = sysUserQueryVo.getCreateTimeEnd();

        if (!StringUtils.isEmpty(username)) {
            wrapper.like(SysUser::getUsername, username);
        }
        //  ge 大于等于
        if (!StringUtils.isEmpty(createTimeBegin)) {
            wrapper.ge(SysUser::getCreateTime, createTimeBegin);
        }
        //  le 小于等于
        if (!StringUtils.isEmpty(createTimeEnd)) {
            wrapper.le(SysUser::getCreateTime, createTimeEnd);
        }

        IPage<SysUser> pageModel = service.page(pageParam, wrapper);

        return Result.ok(pageModel);
    }

    @ApiOperation("添加用户")
    @PostMapping("/save")
    public Result save(@RequestBody SysUser sysUser) {

        // 密码进行加密,使用MD5
        String passwordMD5 = MD5.encrypt(sysUser.getPassword());
        sysUser.setPassword(passwordMD5);

        boolean isSuccess = service.save(sysUser);
        if (isSuccess) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @ApiOperation("根据id查询")
    @GetMapping("/get/{id}")
    public Result get(@PathVariable Long id) {
        SysUser sysUser = service.getById(id);
        return Result.ok(sysUser);
    }

    @ApiOperation("修改")
    @PutMapping("/update")
    public Result update(@RequestBody SysUser sysUser) {
        boolean isSuccess = service.updateById(sysUser);
        if (isSuccess) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @ApiOperation("删除")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable Long id) {
        boolean isSuccess = service.removeById(id);
        if (isSuccess) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @ApiOperation("批量删除")
    @DeleteMapping("/batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList) {
        boolean isSuccess = service.removeByIds(idList);
        if (isSuccess) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

}

