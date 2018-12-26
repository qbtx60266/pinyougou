package com.pinyougou.pay.service;

import java.util.Map;

/**
 * 微信支付接口
 * @author FallingSkies
 * @date 2018/12/26 16:08
 */
public interface WeixinPayService {
    /**
     * 本地交易类型，生成二维码
     * @param out_trade_no
     * @param total_fee
     * @return
     */
    Map createNative(String out_trade_no,String total_fee);


    /**
     * 查询支付订单状态
     * @param out_trade_no
     * @return
     */
    Map queryPayStatus(String out_trade_no);
}
