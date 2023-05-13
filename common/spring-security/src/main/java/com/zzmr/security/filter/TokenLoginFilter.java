package com.zzmr.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzmr.common.jwt.JwtHelper;
import com.zzmr.common.result.ResponseUtil;
import com.zzmr.common.result.Result;
import com.zzmr.common.result.ResultCodeEnum;
import com.zzmr.security.custom.CustomUser;
import com.zzmr.vo.system.LoginVo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {

    // 构造方法
    public TokenLoginFilter(AuthenticationManager authenticationManager) {
        this.setAuthenticationManager(authenticationManager);
        this.setPostOnly(false);
        // 指定登录接口及提交方式，可以指定任意路径
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/admin/system/index/login", "POST"));
    }

    // 登录认证
    // 获取输入的用户名和密码，调用方法认证
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            // 获取用户输入的信息
            LoginVo loginVo = new ObjectMapper().readValue(request.getInputStream(), LoginVo.class);

            // 封装对象
            Authentication authenticationToken = new UsernamePasswordAuthenticationToken(loginVo.getUsername(), loginVo.getPassword());

            // 调用方法
            return this.getAuthenticationManager().authenticate(authenticationToken);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 认证成功调用方法
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {

        // 获取到当前认证用户对象
        CustomUser customUser = (CustomUser) authResult.getPrincipal();
        // 生成token字符串
        String token = JwtHelper.createToken(customUser.getSysUser().getId(), customUser.getSysUser().getUsername());
        // 进行返回
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        ResponseUtil.out(response, Result.ok(map));
    }

    // 认证失败调用方法
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {
        ResponseUtil.out(response, Result.build(null, ResultCodeEnum.LOGIN_ERROR));
    }

}
