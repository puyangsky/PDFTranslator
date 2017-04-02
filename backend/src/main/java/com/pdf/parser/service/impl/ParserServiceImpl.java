package com.pdf.parser.service.impl;

import com.pdf.parser.service.ParserService;
import com.pdf.parser.util.OkHttpUtil;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Author: puyangsky
 * Date: 17/4/1
 */

@Service
public class ParserServiceImpl implements ParserService {

    private final int THRES = 5000;

    @Override
    public String parse(String filePath) {
        String result = null;
        FileInputStream is = null;
        PDDocument document = null;
        try {
            is = new FileInputStream(filePath);
            PDFParser parser = new PDFParser(is);
            parser.parse();
            document = parser.getPDDocument();
            PDFTextStripper stripper = new PDFTextStripper();
            result = stripper.getText(document);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (document != null) {
                try {
                    document.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 调用谷歌翻译API
     * @param content 待翻译内容
     * */
    @Override
    public String translate(String content) {
        StringBuilder sb = new StringBuilder();
        try {
            //一次调用传入过大的字符串会返回403，应将字符串切片，再用StringBuilder拼接起来
//            System.out.println(content.length());
            if (content.length() > 5000) {
                int beginIndex = 0;
                while (beginIndex <= content.length()) {
                    int newIndex = getSubContentIndex(content, beginIndex);
                    String subContent = content.substring(beginIndex, newIndex);
                    sb.append(translate0(subContent));
                    beginIndex = newIndex;
                }
             }else {
                sb.append(translate0(content));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(sb.toString());
        return sb.toString();
    }

    private String translate0(String content) throws IOException {
        String query = null;
        String url = "https://translate.google.cn/translate_a/single?client=t&sl=en&tl=zh-CN&hl=zh-CN" +
                "&dt=at&dt=bd&dt=ex&dt=ld&dt=md&dt=qca&dt=rw&dt=rm&dt=ss&dt=t&ie=UTF-8&oe=UTF-8&source=btn" +
                "&ssel=3&tsel=6&kc=0&tk=830717.720155&q=";
        query = URLEncoder.encode(content, "UTF-8");
        return OkHttpUtil.get(url + query);
    }

    private int getSubContentIndex(String content, int curIndex) {
        //如果从当前下标往后第5000个字符不为结束符'.'，则往前查找直到找到'.'
        int newIndex = curIndex + THRES;
        if (newIndex >= content.length()) {
            return content.length();
        }
        if (content.charAt(newIndex) != '.') {
            while (true) {
                newIndex--;
                if (content.charAt(newIndex) == '.')
                    break;
            }
            return newIndex;
        }else {
            return newIndex;
        }
    }
}
