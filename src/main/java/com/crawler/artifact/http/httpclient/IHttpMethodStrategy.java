package com.crawler.artifact.http.httpclient;

import java.util.Map;

/**
 * Created by liuzhixiong on 2018/9/20.
 * 请求策略接口
 */

public interface IHttpMethodStrategy {

    Response request(HttpCrawler lc, String url, Object params, Map<String, String> headers, String charset);

}
