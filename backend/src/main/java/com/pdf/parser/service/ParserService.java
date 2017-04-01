package com.pdf.parser.service;

import org.springframework.stereotype.Service;

/**
 * Author: puyangsky
 * Date: 17/4/1
 */
@Service
public interface ParserService {
    String parse(String filepath);
    String translate(String content);
}
