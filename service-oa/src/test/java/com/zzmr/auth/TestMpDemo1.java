package com.zzmr.auth;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zzmr.auth.mapper.SysRoleMapper;
import com.zzmr.model.system.SysRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class TestMpDemo1 {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Test
    @DisplayName("查询所有记录 ")
    public void getAll() {
        List<SysRole> sysRoles = sysRoleMapper.selectList(null);
        sysRoles.forEach(System.out::println);
    }

    @Test
    @DisplayName("添加")
    public void add() {
        SysRole sysRole = new SysRole();
        sysRole.setRoleName("角色管理员");
        sysRole.setRoleCode("role");
        sysRole.setDescription("角色管理员123");
        // rows 影响行数
        int rows = sysRoleMapper.insert(sysRole);
        System.out.println(rows);
        System.out.println(sysRole.getId());
    }

    @Test
    @DisplayName("修改")
    public void update() {
        //        根据id查询
        SysRole role = sysRoleMapper.selectById(12);
        //        设置修改值
        role.setRoleName("zzmr角色管理员");
        //        调用方法实现最终修改
        int rows = sysRoleMapper.updateById(role);

        System.out.println(rows);
    }

    /**
     * 配置文件中加入配置
     * 此为默认值，不需要修改
     * mybatis-plus:
     * global-config:
     * db-config:
     * logic-delete-value: 1
     * logic-note-delete-value: 0
     */
    @Test
    @DisplayName("根据id删除")
    public void deleteById() {
        int rows = sysRoleMapper.deleteById(12);
    }

    @Test
    @DisplayName("批量删除")
    public void testDeleteBatchIds() {
        int result = sysRoleMapper.deleteBatchIds(Arrays.asList(10, 11));
        System.out.println(result);
    }

    @Test
    @DisplayName("条件查询")
    public void querySelect() {
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_code", "admin");
        List<SysRole> sysRoles = sysRoleMapper.selectList(queryWrapper);
        sysRoles.forEach(System.out::println);
    }

    //  好处在于，不用在乎表中字段了，可以直接使用实体类中的属性来查询
    @Test
    @DisplayName("条件查询")
    public void querySelectLambda() {
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRole::getRoleName, "总经理");
        List<SysRole> sysRoles = sysRoleMapper.selectList(wrapper);
        sysRoles.forEach(System.out::println);
    }


}
