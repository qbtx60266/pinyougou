package com.pinyougou.page.service.impl;

import com.pinyougou.page.service.ItemPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * 生成网页监听类
 * @author FallingSkies
 * @date 2018/12/22 10:29
 */
@Component
public class PageListener implements MessageListener {

    @Autowired
    private ItemPageService itemPageService;


    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            String text = textMessage.getText();
            System.out.println("接收到消息" + text);
            boolean b = itemPageService.genItemHtml(Long.parseLong(text));
            System.out.println("结果:" + b);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
