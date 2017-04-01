package com.pdf.parser.service;

/**
 * Author: puyangsky
 * Date: 17/4/1
 */
public interface RabbitService {
    void send(String queue, String msg);
}
