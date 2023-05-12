package com.zzmr.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zzmr.auth.service.SysMenuService;
import com.zzmr.auth.service.SysUserService;
import com.zzmr.common.config.exception.ZzmrException;
import com.zzmr.common.jwt.JwtHelper;
import com.zzmr.common.result.Result;
import com.zzmr.common.utils.MD5;
import com.zzmr.model.system.SysUser;
import com.zzmr.vo.system.LoginVo;
import com.zzmr.vo.system.RouterVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "后台登录管理")
@RequestMapping("/admin/system/index")
public class IndexController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMenuService sysMenuService;

    // login
    @PostMapping("/login")
    public Result login(@RequestBody LoginVo loginVo) {
        // {"code":200,"data":{"token":"admin-token"}}
        // Map<String, Object> map = new HashMap<>();
        // map.put("token", "admin-token");
        // System.out.println("test");
        // return Result.ok(map);
        // 1 获取输入用户名和密码
        String username = loginVo.getUsername();
        String password = loginVo.getPassword();
        // 2 根据用户名查询数据库
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, username);
        SysUser sysUser = sysUserService.getOne(wrapper);
        // 3 用户信息是否存在
        if (sysUser == null) {
            throw new ZzmrException(201, "用户不存在");
        }
        // 4 判断用户密码
        // 将用户输入的密码进行MD5加密,然后和数据库中的密码进行比较-因为MD5加密之后是不可逆的
        if (!MD5.encrypt(password).equals(sysUser.getPassword())) {
            throw new ZzmrException(201, "密码不正确");
        }
        // 5 判断用户是否被禁用
        if (sysUser.getStatus() == 0) {
            throw new ZzmrException(201, "用户被禁用");
        }
        // 6使用JWT根据用户id和用户名称生成token字符串
        String token = JwtHelper.createToken(sysUser.getId(), username);
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        return Result.ok(map);

    }

    // info
    @GetMapping("/info")
    public Result info(HttpServletRequest request) {
        /*{
            "code":20000,
                "data":{"roles":["admin"],
            "introduction":"I am a super administrator",
                    "avatar":"https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif",
                    "name":"Super Admin"}
        }*/


        // 1 请求头中获取用于信息(获取请求头token字符串)
        String token = request.getHeader("token");
        System.out.println("查看是否有token" + token);
        // 2 从token字符串获取用户id,或者用户名称
        Long userId = JwtHelper.getUserId(token);
        System.out.println("测试是否有userId" + userId);
        // 3 根据用户id查询数据库,把用户信息获取出来
        SysUser user = sysUserService.getById(userId);
        // 4 根据用户id获取用户可以操作的菜单列表---查询数据库动态构建路由结构,进行最终的显示
        List<RouterVo> routerList = sysMenuService.findUserMenuListByUserId(user.getId());
        // 5 根据用户id获取用户可以操作按钮列表
        List<String> permsList = sysMenuService.findUserPermsByUserId(user.getId());

        Map<String, Object> map = new HashMap<>();
        map.put("roles", "[admin]");
        map.put("name", user.getName());
        map.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");

        // TODO 4 返回用户可以操作的菜单
        map.put("routers", routerList);
        // TODO 5 返回用户可以操作按钮
        map.put("buttons", permsList);

        // 6 返回相应的数据
        return Result.ok(map);

    }

    // 退出的方法
    @PostMapping("/logout")
    public Result logout() {
        return Result.ok();
    }

}
