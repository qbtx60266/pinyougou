package com.pinyougou.pay.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.wxpay.sdk.WXPayUtil;
import com.pinyougou.pay.service.WeixinPayService;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信支付实现类
 * @author FallingSkies
 * @date 2018/12/26 16:39
 */
@Service
public class WeixinPayServiceImpl implements WeixinPayService {
    @Value("${appid}")
    private String appid;
    @Value("${partner}")
    private String partner;
    @Value("${partnerkey}")
    private String partnerkey;

    @Override
    public Map createNative(String out_trade_no, String total_fee) {
        //参数封装
        Map param = new HashMap();
        //公众号id
        param.put("appid",appid);
        //商户id
        param.put("mch_id",partner);
        //随机字符串
        param.put("nonce_str", WXPayUtil.generateNonceStr());
        //商品描述
        param.put("body","品优购");
        //交易订单号
        param.put("out_trade_no",out_trade_no);
        //金额(分)
        param.put("total_fee",total_fee);
        param.put("spbill_create_ip","127.0.0.1");
        param.put("notify_url","http://www.itcast.cn");
        //交易类型
        param.put("trade_type","NATIVE");



        //发送请求
        try {
            String paramXml = WXPayUtil.generateSignedXml(param, partnerkey);
            System.out.println("参数" + paramXml);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //获取结果
        return null;
    }
}
