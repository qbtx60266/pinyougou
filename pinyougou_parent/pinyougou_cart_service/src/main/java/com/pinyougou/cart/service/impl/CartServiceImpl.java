package com.pinyougou.cart.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.cart.service.CartService;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbOrderItem;
import com.pinyougou.pojogroup.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author FallingSkies
 * @date 2018/12/24 18:12
 */
@Service
public class CartServiceImpl implements CartService {


        @Autowired
        private TbItemMapper itemMapper;

        @Autowired
        private RedisTemplate redisTemplate;

    /**
     * 把商品加入到购物车
     * @param cartList
     * @param itemId
     * @param num
     * @return
     */
    @Override
    public List<Cart> addGoodsToCartList(List<Cart> cartList, Long itemId, Integer num) {
        //根据skuId查询商品明细的对象
        TbItem item = itemMapper.selectByPrimaryKey(itemId);
        if (item == null){
            throw new RuntimeException("商品不存在");
        }
        if (!"1".equals(item.getStatus())){
            throw new RuntimeException("商品状态不合法");
        }
        //根据sku对象得到商家id
        String sellerId = item.getSellerId();

        //根据商家id再购物车列表中查询购物车对象
        Cart cart = searchCartBySellerId(cartList, sellerId);

        //如果购物车列表中不存在该商家的购物车
        //创建一个新的购物车对象
        if (cart == null){
            cart = new Cart();
            cart.setSellerId(sellerId);
            cart.setSellerName(item.getSeller());
            //创建购物车明细列表
            List<TbOrderItem> orderItemList = new ArrayList<>();
            //创建新的购物车明细对象
            TbOrderItem orderItem = createOrderItem(item, num);
            orderItemList.add(orderItem);
            cart.setOrderItemList(orderItemList);
            //将新的购物车对象添加到购物车列表中
            cartList.add(cart);
        }else {
            //如果购物车列表中存在该商家的购物车
            //判断该商品是否存在于购物车明细列表中
            TbOrderItem orderItem = searchOrderItemByItemId(cart.getOrderItemList(), itemId);
            //若不存在创建新的购物车明细对象，并添加到购物车的明细列表中
            if (orderItem==null){
                orderItem = createOrderItem(item, num);
                cart.getOrderItemList().add(orderItem);
            }else {
                //如果存在，就在原有的数量上添加数量，并且更新金额
                //更改数量
                orderItem.setNum(orderItem.getNum() + num);
                //金额
                orderItem.setTotalFee(new BigDecimal(orderItem.getNum() * orderItem.getPrice().doubleValue()));
                //当明细的数量小于等于0，移除明细
                if (orderItem.getNum()<=0){
                    cart.getOrderItemList().remove(orderItem);
                }
                //当购物车明细数量为0时，在购物车列表中移除此购物车
                if (cart.getOrderItemList().size() == 0){
                    cartList.remove(cart);
                }
            }



        }

        return cartList;
    }

    /**
     * 从redis取购物车
     * @param username
     * @return
     */
    @Override
    public List<Cart> findCartListFromRedis(String username) {
        List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("cartList").get(username);
        if (cartList == null){
            cartList = new ArrayList<>();
        }
        return cartList;
    }


    /**
     * 向redis存购物车
     * @param username
     * @param cartList
     */
    @Override
    public void saveCartListToRedis(String username, List<Cart> cartList) {
        redisTemplate.boundHashOps("cartList").put(username,cartList);
    }

    /**
     * 合并购物车
     * @param cartList1
     * @param cartList2
     * @return
     */
    @Override
    public List<Cart> mergeCartList(List<Cart> cartList1, List<Cart> cartList2) {
        for (Cart cart : cartList2) {
            for (TbOrderItem orderItem : cart.getOrderItemList()) {
                addGoodsToCartList(cartList1,orderItem.getItemId(),orderItem.getNum());
            }
        }
        return cartList1;
    }


    /**
     * 判断购物车中是否存在该商品
     * @param cartList
     * @param sellerId
     * @return
     */
    private Cart searchCartBySellerId(List<Cart> cartList,String sellerId){
        for (Cart cart : cartList) {
            if (cart.getSellerId().equals(sellerId)){
                return cart;
            }
        }
        return null;
    }


    /**
     *  船舰购物车明细对象
     * @return
     */
    private TbOrderItem createOrderItem(TbItem item,Integer num){
        TbOrderItem orderItem = new TbOrderItem();
        orderItem.setGoodsId(item.getGoodsId());
        orderItem.setItemId(item.getId());
        orderItem.setNum(num);
        orderItem.setPicPath(item.getImage());
        orderItem.setPrice(item.getPrice());
        orderItem.setSellerId(item.getSellerId());
        orderItem.setTitle(item.getTitle());
        orderItem.setTotalFee(new BigDecimal(item.getPrice().doubleValue() * num));
        return orderItem;
    }

    /**
     * 根据skuid在购物车明细列表中查询购物车对象
     * @param orderItemList
     * @param itemId
     * @return
     */
    private TbOrderItem searchOrderItemByItemId(List<TbOrderItem> orderItemList,Long itemId){
        for (TbOrderItem orderItem : orderItemList) {
            if (orderItem.getItemId().longValue()==itemId.longValue()){
                return orderItem;
            }
        }
        return null;
    }


}
