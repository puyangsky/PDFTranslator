package com.pdf.parser.util;

import com.pdf.parser.BaseTest;
import org.junit.Test;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * Author: puyangsky
 * Date: 17/4/2
 */
public class OkHTTPTest extends BaseTest {

    @Test
    public void test() throws IOException {
        String url = "https://translate.google.cn/translate_a/single?client=t&sl=en&tl=zh-CN&hl=zh-CN" +
                "&dt=at&dt=bd&dt=ex&dt=ld&dt=md&dt=qca&dt=rw&dt=rm&dt=ss&dt=t&ie=UTF-8&oe=UTF-8&source=btn" +
                "&ssel=3&tsel=6&kc=0&tk=830717.720155&q=";
        String query = URLEncoder.encode("fuck you", "UTF-8");
        System.out.println(query);
        System.out.println(OkHttpUtil.get(url + query));
    }

    @Test
    public void test1() {
        String a = "abc";
        System.out.println(a.substring(0,3));
    }
}
