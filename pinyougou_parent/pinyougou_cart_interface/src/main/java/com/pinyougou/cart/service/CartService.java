package com.pinyougou.cart.service;

import com.pinyougou.pojogroup.Cart;

import java.util.List;

/**
 * 购物车服务接口
 * @author FallingSkies
 * @date 2018/12/24 18:09
 */
public interface CartService {
    /**
     * 添加商品到购物车
     * @param list
     * @param itemId
     * @param num
     * @return
     */
    List<Cart> addGoodsToCartList(List<Cart> list,Long itemId,Integer num);


    /**
     * 从redis提取购物车
     * @param username
     * @return
     */
    List<Cart> findCartListFromRedis(String username);

    /**
     * 将购物车列表存入redis
     * @param username
     * @param cartList
     */
    void saveCartListToRedis(String username,List<Cart> cartList);

}
