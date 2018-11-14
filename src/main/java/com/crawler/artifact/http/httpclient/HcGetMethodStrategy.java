package com.crawler.artifact.http.httpclient;

import java.util.Map;

/**
 * Created by liuzhixiong on 2018/9/20.
 * GET请求方法实现
 */

public class HcGetMethodStrategy extends DefaultHttpMethodStrategy {

    @Override
    public Response requestWithoutData(HttpCrawler lc, String url, Map<String, String> headers, String charset) {
        Response response = lc.get(url, headers, charset);
        return response;
    }
}
