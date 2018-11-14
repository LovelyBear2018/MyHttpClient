package com.crawler.artifact.http.httpclient;

import java.util.Map;

/**
 * Created by liuzhixiong on 2018/9/20.
 * POST请求方法实现
 */

public class HcPostMethodStrategy extends DefaultHttpMethodStrategy {

    public  Response requestWithMapData(HttpCrawler lc, String url, Map<String, String> params, Map<String, String> headers, String charset){

        Response response = lc.post(url, params, headers);
        return  response;

    }

    public  Response requestWithStringData(HttpCrawler lc, String url, String params, Map<String, String> headers, String charset){

        Response response = lc.post(url, params, headers);
        return  response;

    }

}
