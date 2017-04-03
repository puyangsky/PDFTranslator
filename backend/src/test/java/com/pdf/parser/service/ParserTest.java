package com.pdf.parser.service;

import com.pdf.parser.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Author: puyangsky
 * Date: 17/4/1
 */

public class ParserTest extends BaseTest {

    @Autowired
    private ParserService parserService;

    @Test
    public void parse() {
        String filepath = "/Users/imac/Desktop/1.pdf";
        String trans = parserService.parse(filepath);
        System.out.println("trans = " + trans);
        System.out.println("length:" + trans.length());
//        parserService.translate(filepath);
    }

    @Test
    public void translate() {
        parserService.translate("\"hello\", fuck u");
    }

    @Test
    public void encode() {
        try {
            System.out.println(URLEncoder.encode("\"hello\", fuck u", "UTF-8").replaceAll("\\+", "%20"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
