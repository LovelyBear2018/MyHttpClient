package com.crawler.artifact.http.httpclient;

import org.apache.http.HttpHost;

/**
 * Created by liuzhixiong on 2018/11/14.
 */
public class LoginConfig {
    private int connectTimeout = 30 * 1000;//连接超时
    private int socketTimeout = 20 * 1000;//传输超时
    private HttpHost proxy;
    private String proxyIP = null;//代理ip
    private int proxyPort;//代理端口
    private boolean followRedirects = false;//是否重定向
    private int maxDownloadSize = 1048576;//最大下载文件
    private int maxTryTimesToFetch = 3;//重试次数
    private int maxTotalConnections = 200;//最大连接数
    private int maxConnectionPerHost = 200;//每个路由最大连接数
    private boolean includeHttpsPages = true;//是否抓取https页面

    public HttpHost getProxy() {
        return proxy;
    }
    public void setProxy(HttpHost proxy) {
        this.proxy = proxy;
    }
    public int getConnectTimeout() {
        return connectTimeout;
    }
    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }
    public int getSocketTimeout() {
        return socketTimeout;
    }
    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }
    public String getProxyIP() {
        return proxy.getHostName();
    }
    public void setProxyIP(String proxyIP) {
        this.proxyIP = proxyIP;
    }
    public int getProxyPort() {
        return proxy.getPort();
    }
    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }
    public boolean isFollowRedirects() {
        return followRedirects;
    }
    public void setFollowRedirects(boolean followRedirects) {
        this.followRedirects = followRedirects;
    }
    public int getMaxDownloadSize() {
        return maxDownloadSize;
    }
    public void setMaxDownloadSize(int maxDownloadSize) {
        this.maxDownloadSize = maxDownloadSize;
    }
    public int getMaxTryTimesToFetch() {
        return maxTryTimesToFetch;
    }
    public void setMaxTryTimesToFetch(int maxTryTimesToFetch) {
        this.maxTryTimesToFetch = maxTryTimesToFetch;
    }
    public int getMaxTotalConnections() {
        return maxTotalConnections;
    }
    public void setMaxTotalConnections(int maxTotalConnections) {
        this.maxTotalConnections = maxTotalConnections;
    }
    public int getMaxConnectionPerHost() {
        return maxConnectionPerHost;
    }
    public void setMaxConnectionPerHost(int maxConnectionPerHost) {
        this.maxConnectionPerHost = maxConnectionPerHost;
    }
    public boolean isIncludeHttpsPages() {
        return includeHttpsPages;
    }
    public void setIncludeHttpsPages(boolean includeHttpsPages) {
        this.includeHttpsPages = includeHttpsPages;
    }
    @Override
    public String toString() {
        return "LoginConfig [connectTimeout=" + connectTimeout + ", socketTimeout=" + socketTimeout + ", proxyIP="
                + proxyIP + ", proxyPort=" + proxyPort + ", followRedirects=" + followRedirects + ", maxDownloadSize="
                + maxDownloadSize + ", maxTryTimesToFetch=" + maxTryTimesToFetch + ", maxTotalConnections="
                + maxTotalConnections + ", maxConnectionPerHost=" + maxConnectionPerHost + ", includeHttpsPages="
                + includeHttpsPages + "]";
    }

}
