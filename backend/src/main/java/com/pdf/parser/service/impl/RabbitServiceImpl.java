package com.pdf.parser.service.impl;

import com.pdf.parser.service.RabbitService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Author: puyangsky
 * Date: 17/4/1
 */

@Service
public class RabbitServiceImpl implements RabbitService {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Override
    public void send(String queue, String msg) {
        rabbitTemplate.convertAndSend(queue, msg);
        System.out.println("+++ Send: " + msg);
    }
}
