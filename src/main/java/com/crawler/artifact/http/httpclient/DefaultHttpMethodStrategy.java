package com.crawler.artifact.http.httpclient;

import java.util.Map;

/**
 * Created by liuzhixiong on 2018/9/26.
 * HttpClient 默认实现类
 */

public class DefaultHttpMethodStrategy implements IHttpMethodStrategy {

    /**
     * 上层到本层请求总入口
     * @param lc
     * @param url
     * @param params
     * @param headers
     * @param charset
     * @return
     */
    @Override
    public Response request(HttpCrawler lc, String url, Object params, Map<String, String> headers, String charset) {

        if(params == null){
            return requestWithoutData(lc, url, headers, charset);
        } else if (params instanceof Map) {
            Map<String, String> realParams = (Map<String, String>) params;
            if (realParams.size() == 0) {
                return requestWithoutData(lc, url, headers, charset);
            }
            return requestWithMapData(lc, url, realParams, headers, charset);
        } else if (params instanceof String) {
            return requestWithStringData(lc, url, (String)params, headers, charset);
        } else {
            return null;
        }
    }

    /**
     * 无请求数据,主要指GET方法
     * @param lc
     * @param url
     * @param headers
     * @param charset
     * @return
     */
    public  Response requestWithoutData(HttpCrawler lc, String url, Map<String, String> headers, String charset){
        return null;
    }

    /**
     * 有Map类型请求数据
     * @param lc
     * @param url
     * @param params
     * @param headers
     * @param charset
     * @return
     */
    public  Response requestWithMapData(HttpCrawler lc, String url, Map<String, String> params, Map<String, String> headers, String charset){
        return null;
    }

    /**
     * 有String类型请求数据
     * @param lc
     * @param url
     * @param params
     * @param headers
     * @param charset
     * @return
     */
    public  Response requestWithStringData(HttpCrawler lc, String url, String params, Map<String, String> headers, String charset){
        return null;
    }

}
