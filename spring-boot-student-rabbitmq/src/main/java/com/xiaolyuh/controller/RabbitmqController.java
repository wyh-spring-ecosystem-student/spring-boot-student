package com.xiaolyuh.controller;

import com.xiaolyuh.constants.RabbitConstants;
import com.xiaolyuh.mq.message.SendMessage;
import com.xiaolyuh.mq.sender.RabbitSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yuhao.wang
 */
@RestController
public class RabbitmqController {

    @Autowired
    private RabbitSender rabbitSender;

    @RequestMapping(value = "sendMsg", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Object sendMsg() {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setId(1);
        sendMessage.setAge(20);
        sendMessage.setName("西瓜");

        rabbitSender.sendMessage(RabbitConstants.MQ_EXCHANGE_SEND_AWARD, RabbitConstants.MQ_ROUTING_KEY_SEND_COUPON, sendMessage);
        return "发送成";
    }

}