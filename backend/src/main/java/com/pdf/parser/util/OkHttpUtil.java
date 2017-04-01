package com.pdf.parser.util;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.springframework.beans.factory.InitializingBean;

import java.io.IOException;

/**
 * Author: puyangsky
 * Date: 17/4/1
 */
public class OkHttpUtil implements InitializingBean {

    private static OkHttpClient client = new OkHttpClient();

    @Override
    public void afterPropertiesSet() throws Exception {
        client = new OkHttpClient();
    }

    /**
     * HTTP GET方法
     * */
    public static String get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
