package com.zzmr.auth.controller;

import com.zzmr.common.result.Result;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Api(tags = "后台登录管理")
@RequestMapping("/admin/system/index")
public class IndexController {

    // login
    @PostMapping("/login")
    public Result login() {
        // {"code":200,"data":{"token":"admin-token"}}
        Map<String, Object> map = new HashMap<>();
        map.put("token", "admin-token");
        return Result.ok(map);
    }

    // info
    @GetMapping("/info")
    public Result info() {
        /*{
            "code":20000,
                "data":{"roles":["admin"],
            "introduction":"I am a super administrator",
                    "avatar":"https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif",
                    "name":"Super Admin"}
        }*/
        Map<String, Object> map = new HashMap<>();
        map.put("roles", "[admin]");
        map.put("name", "admin");
        map.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        return Result.ok(map);
    }

    // 退出的方法
    @PostMapping("/logout")
    public Result logout() {
        return Result.ok();
    }

}
