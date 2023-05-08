package com.zzmr.auth;

import com.zzmr.auth.service.SysRoleService;
import com.zzmr.model.system.SysRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TestDemo2 {

    @Autowired
    private SysRoleService service;

    @Test
    public void testService() {
        List<SysRole> list = service.list();
        list.forEach(System.out::println);
    }

}
