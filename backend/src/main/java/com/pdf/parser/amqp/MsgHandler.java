package com.pdf.parser.amqp;

import com.pdf.parser.service.ParserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;


/**
 * Author: puyangsky
 * Date: 17/4/1
 * 接收RabbitMQ中的消息，并调用解析服务进行解析
 */
@Component
public class MsgHandler {

    @Autowired
    private ParserService parserService;

    @RabbitListener(queues = "default")
    @RabbitHandler
    public void process(String msg) {
        System.out.println("[*] Receive from : " + msg);
        msg = decode(msg);
        if (StringUtils.isEmpty(msg)) {
            System.err.println("[x] Receive empty path...");
            return;
        }
        String res = parserService.parse(msg);
        System.out.println("res" + res);
        String translate = parserService.translate(res);
    }

    private String decode(String msg) {
//        long start = System.currentTimeMillis();
        if(StringUtils.isEmpty(msg))
            return "";
        String[] charArray = msg.split(",");
        StringBuilder sb = new StringBuilder();
        for (String ch : charArray) {
            int i = Integer.valueOf(ch);
            char c = (char) i;
            sb.append(c);
        }

//        System.out.println(sb.toString());
//        long end = System.currentTimeMillis() - start;
//        System.out.println("time: " + end);
        return sb.toString();
    }

}
