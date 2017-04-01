package com.pdf.parser.amqp;

import com.pdf.parser.service.ParserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


/**
 * Author: puyangsky
 * Date: 17/4/1
 */
@PropertySource(value = "classpath:rabbit.properties", ignoreResourceNotFound = true)
@Component
public class MsgHandler {

    @Autowired
    private ParserService parserService;

    @RabbitListener(queues = "hello")
    @RabbitHandler
    public void process(String msg) {
        System.out.println("[*] Receive from : " + msg);
        if (StringUtils.isEmpty(msg)) {
            System.err.println("[x] Receive empty path...");
            return;
        }
        String res = parserService.parse(msg);
        System.out.println("" + res);
    }

}
