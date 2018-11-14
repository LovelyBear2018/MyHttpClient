package com.crawler.artifact.http.httpclient;

import java.util.Map;

/**
 * Created by liuzhixiong on 2018/9/20.
 * OPTION请求方法实现
 */

public class HcOptionMethodStrategy extends DefaultHttpMethodStrategy {

    @Override
    public Response requestWithoutData(HttpCrawler lc, String url, Map<String, String> headers, String charset) {

        Response response = lc.option(url, headers, charset);

        return  response;
    }

}
