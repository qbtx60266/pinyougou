package com.pinyougou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.pinyougou.cart.service.CartService;
import com.pinyougou.pojogroup.Cart;
import entity.Result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import util.CookieUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 购物车控制层
 *
 * @author FallingSkies
 * @date 2018/12/24 21:54
 */
@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private HttpServletRequest request;

    @Reference
    private CartService cartService;

    @Autowired
    private HttpServletResponse response;

    /**
     * 将商品加入购物车
     *
     * @param itemId
     * @param num
     * @return
     */
    @RequestMapping("/addGoodsToCartList")
    public Result addGoodsToCartList(Long itemId, Integer num) {
        //当前登陆账号
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(name);

        try {
            //提取购物车
            List<Cart> cartList = findCartList();
            //调用服务方法操作购物车
            cartList = cartService.addGoodsToCartList(cartList, itemId, num);
            //如果未登录
            if ("anonymousUser".equals(name)) {
                //将新的购物车存入cookie
                String cartListString = JSON.toJSONString(cartList);
                CookieUtil.setCookie(request, response, "cartList", cartListString, 86400, "UTF-8");
            } else {
                //如果登陆
                cartService.saveCartListToRedis(name,cartList);
            }
            return new Result(true, "存入购物车成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "存入购物车失败");
        }

    }

    /**
     * 从cookie中提取购物车
     *
     * @return
     */
    @RequestMapping("/findCartList")
    public List<Cart> findCartList() {
        //当前登陆账号
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(username);
        //从cookie中提取购物车
        String cartListString = CookieUtil.getCookieValue(request, "cartList", "UTF-8");
        if (cartListString == null || cartListString.length() == 0) {
            cartListString = "[]";
        }
        List<Cart> cartList_cookie = JSON.parseArray(cartListString, Cart.class);
        if ("anonymousUser".equals(username)) {
            //未登录
            return cartList_cookie;
        } else {
            //已登陆,获取redis购物车
            List<Cart> cartList_redis = cartService.findCartListFromRedis(username);
            //判断本地购物车中是否存在数据
            if (cartList_cookie.size() > 0){
                //合并购物车
                List<Cart> cartList = cartService.mergeCartList(cartList_cookie, cartList_redis);
                //将合并后的购物车存入redis
                cartService.saveCartListToRedis(username,cartList);
                //清除本地购物车
                CookieUtil.deleteCookie(request,response,"cartList");

                return cartList;
            }

            return cartList_redis;
        }
    }
}
