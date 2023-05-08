package com.zzmr.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzmr.auth.service.SysRoleService;
import com.zzmr.common.result.Result;
import com.zzmr.model.system.SysRole;
import com.zzmr.vo.system.AssginRoleVo;
import com.zzmr.vo.system.SysRoleQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/system/sysRole")
@Api(tags = "角色管理接口")
public class SysRoleController {

    //    注入service
    @Autowired
    private SysRoleService sysRoleService;

    // 1 查询所有角色 和 当前用户所属角色
    @ApiOperation("获取角色")
    @GetMapping("/toAssign/{userId}")
    public Result toAssign(@PathVariable Long userId) {
        Map<String, Object> map = sysRoleService.findRoleDataByUserId(userId);
        return Result.ok(map);
    }

    // 2 为用户分配角色
    @ApiOperation("为用户分配角色")
    @PostMapping("/doAssign")
    // 应该是收到一个用户id,然后和n个角色id
    public Result doAssign(@RequestBody AssginRoleVo assginRoleVo) {
        sysRoleService.doAssign(assginRoleVo);
        return Result.ok();
    }


    //    查询所有的角色
    /*@GetMapping("/findAll")
    public List<SysRole> findAll() {
        return sysRoleService.list();
    }*/

    @GetMapping("/findAll")
    @ApiOperation("查询所有的角色")
    public Result findAll() {
        // 模拟异常效果
        /*try {
            int i = 10 / 0;
        } catch (Exception e) {
            // 抛出自定义异常
            throw new ZzmrException(20001,"执行了自定义异常处理");
        }*/
        return Result.ok(sysRoleService.list());
    }

    // 条件分页查询
    // page 当前页 limit 每页显示的记录数
    @ApiOperation("条件分页查询")
    @GetMapping("/{page}/{limit}")
    public Result pageQueryRole(@PathVariable Long page, @PathVariable Long limit,
                                SysRoleQueryVo sysRoleQueryVo) {
        // 调用service的方法实现
        // 1. 创建page对象，传递分页相关参数
        Page<SysRole> pageParam = new Page<>(page, limit);
        // 2. 封装条件，判断条件是否为空，不为空进行封装
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        String roleName = sysRoleQueryVo.getRoleName();
        if (!StringUtils.isEmpty(roleName)) {
            // 不为空，封装
            // 下面的lambda表达式相当于取数据库中roleName的字段名和roleName进行模糊搜索
            wrapper.like(SysRole::getRoleName, roleName);
        }
        // 3. 调用方法实现
        IPage<SysRole> pageModel = sysRoleService.page(pageParam, wrapper);
        return Result.ok(pageModel);
    }

    @ApiOperation("添加")
    @PostMapping("/save")
    public Result save(@RequestBody SysRole sysRole) {
        boolean isSuccess = sysRoleService.save(sysRole);
        if (isSuccess) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    // 修改角色-根据id查询
    @ApiOperation("根据id查询")
    @GetMapping("/get/{id}")
    public Result get(@PathVariable Long id) {
        SysRole sysRole = sysRoleService.getById(id);
        return Result.ok(sysRole);
    }

    // 修改角色-最终修改
    @ApiOperation("修改")
    @PutMapping("/update")
    public Result update(@RequestBody SysRole sysRole) {
        boolean isSuccess = sysRoleService.updateById(sysRole);
        if (isSuccess) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    // 根据id删除
    @ApiOperation("根据id删除")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable Long id) {
        boolean isSuccess = sysRoleService.removeById(id);
        if (isSuccess) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    // 批量删除
    // 前端传入的数据数组样式: [1,2,3]
    // 这里有一个对应关系:json的数组会转换为Java中的list集合
    @ApiOperation("批量删除")
    @DeleteMapping("/batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList) {
        boolean isSuccess = sysRoleService.removeByIds(idList);
        if (isSuccess) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

}
