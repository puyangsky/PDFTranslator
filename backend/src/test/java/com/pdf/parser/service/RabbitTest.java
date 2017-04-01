package com.pdf.parser.service;

import com.pdf.parser.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Author: puyangsky
 * Date: 17/4/1
 */
public class RabbitTest extends BaseTest{

    @Autowired
    private RabbitService rabbitService;

    @Test
    public void test() {
        rabbitService.send("hello", "hello");
    }
}
