package com.pinyougou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pay.service.WeixinPayService;
import entity.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import util.IdWorker;

import java.util.Map;

/**
 * 支付控制层
 * @author FallingSkies
 * @date 2018/12/26 18:15
 */
@RestController
@RequestMapping("/pay")
public class PayController {

    @Reference
    private WeixinPayService weixinPayService;

    /**
     * 创建订单
     * @return
     */
    @RequestMapping("/createNative")
    public Map createNative(){
        IdWorker idWorker = new IdWorker();
        return weixinPayService.createNative(idWorker.nextId() + "","1");
    }

    @RequestMapping("/queryPayStatus")
    public Result queryPayStatus(String out_trade_no){
        Result result = null;
        while (true){
            //调用查询
            Map<String,String> map = weixinPayService.queryPayStatus(out_trade_no);
            if (map == null){
                result = new Result(false,"支付发生错误");
                break;
            }
            //支付成功
            if ("SUCCESS".equals(map.get("trade_state"))){
                result = new Result(true,"支付成功");
                break;
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
