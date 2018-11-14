package com.crawler.artifact.http.environment;

import org.apache.http.HttpHost;

/**
 * Created by liuzhixiong on 2018/11/14.
 */
public class ProxyManager {
    /**
     * String类型转为HttpHost代理
     * @param proxy
     * @return
     */
    public static HttpHost getHttpHost(String proxy){
        String[] arr = proxy.split(":");
        return new HttpHost(arr[0], Integer.valueOf(arr[1]));
    }

    /**
     * Object类型转为HttpHost代理
     * @param proxy
     * @return
     */
    public static HttpHost getHttpHost(Object proxy){
        if(proxy instanceof String){
            return getHttpHost((String)proxy);
        } else if(proxy instanceof HttpHost){
            return (HttpHost)proxy;
        } else {
            throw new IllegalArgumentException("proxy must be String or HttpHost.");
        }
    }
}
