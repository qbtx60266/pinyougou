package com.pinyougou.shop.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * 与登陆有关
 * @author FallingSkies
 * @date 2018/12/10 20:18
 */


@RestController
@RequestMapping("/login")
public class LoginController {
    /**
     * 获取登录名
     * @return
     */
    @RequestMapping("/name")
    public Map name(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Map map = new HashMap<>();
        map.put("loginName",name);
        return map;
    }
}
