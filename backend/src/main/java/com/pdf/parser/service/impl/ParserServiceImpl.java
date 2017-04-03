package com.pdf.parser.service.impl;

import com.pdf.parser.service.ParserService;
import com.pdf.parser.util.OkHttpUtil;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: puyangsky
 * Date: 17/4/1
 */

@Service
@PropertySource(value = "classpath:url.properties", ignoreResourceNotFound = true)
public class ParserServiceImpl implements ParserService {

    private final int THRES = 5000;

    @Value("${google.translate.url}")
    private String googleUrl;

    @Value("${google.translate.tk.url}")
    private String tkUrl;

    @Value("${google.translate.tk.tkk}")
    private String TKK;

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

    /**
     * 真正的翻译方法
     * 其中tk由待翻译字符串和TKK生成
     * */
    private String translate0(String content) throws IOException {
        String query = null;
        //处理java URLEncoder.encode方法将空格转为+号的问题
        query = URLEncoder.encode(content, "UTF-8").replaceAll("\\+", "%20").replaceAll("\\*", "%2A");
        //获取tk
        String tk = OkHttpUtil.get(tkUrl + query + "&TKK=" + TKK);
        //获取翻译结果
        String result = OkHttpUtil.get(googleUrl + tk + "&q=" + query);
        //正则表达式处理结果
        Pattern pattern = Pattern.compile("^\\[\\[\\[\"(.*?)\",.*$");
        Matcher matcher = pattern.matcher(result);
        if (matcher.find()) {
            result = matcher.group(1);
        }
        return result;
    }

    /**
     * 由于谷歌翻译每次只能查询5000个字符，
     * 所以如果查询字符超长需要进行切片查询
     * */
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
