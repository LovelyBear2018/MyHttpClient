package com.crawler.artifact.service;

import org.apache.http.HttpHost;

/**
 * Created by liuzhixiong on 2018/9/27.
 */
public class CrawlerResult {

    int resultCode = 0;
    HttpHost proxy;

    public int getResultCode() {
        return resultCode;
    }

    public HttpHost getProxy() {
        return proxy;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public void setProxy(HttpHost proxy) {
        this.proxy = proxy;
    }

    @Override
    public String toString() {
        return "CrawlerResult{" +
                "resultCode=" + resultCode +
                ", proxy='" + proxy + '\'' +
                '}';
    }
}
