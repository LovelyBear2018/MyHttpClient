package com.crawler.artifact.http.httpclient;

/**
 * Created by liuzhixiong on 2018/11/14.
 */
import java.util.Map;

import org.apache.http.HttpHost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;

public class HttpCrawler {

    private String DEFLAUT_CHARSET = "utf-8";
    private LcStatus status = LcStatus.INIT;
    HttpComponentsUtil httpComponets = new HttpComponentsUtil(new LoginConfig());

    public HttpCrawler() {
    }

    public HttpCrawler(String _DEFLAUT_CHARSET, LoginConfig loginConfig) {
        this.httpComponets = new HttpComponentsUtil(loginConfig);
    }

    public LcStatus getStatus() {
        return status;
    }

    public void setStatus(LcStatus lcStatus) {
        status = lcStatus;
    }

/////////////////////////////////////////////post请求//////////////////////////////////////////////////

    public Response post(String url, Map<String, String> params, Map<String, String> headers) {
        return httpComponets.post(url, params, headers);
    }

    public Response post(String url, String params, Map<String, String> headers) {
        return httpComponets.post(url, params, headers);
    }

    public Response post(String url, Map<String, String> params) {
        return httpComponets.post(url, params);
    }

    public Response post(String url, Map<String, String> params, String charset) {
        return httpComponets.post(url, params, charset);
    }

    public Response post(String url, String params) {
        return httpComponets.post(url, params);
    }

/////////////////////////////////////////////get请求//////////////////////////////////////////////////

    public Response get(String url) {
        Response response = new Response();
        response = httpComponets.get(url);
        return response;
    }

    public Response get(String url, String charset) {
        Response response = new Response();
        response = httpComponets.get(url, charset);
        return response;
    }

    public Response get(String url, Map<String, String> headers) {
        Response response = new Response();
        response = httpComponets.get(url, headers);
        return response;
    }

    public Response get(String url, Map<String, String> headers, String charset) {
        Response response = new Response();
        response = httpComponets.get(url, headers, charset);
        return response;
    }

/////////////////////////////////////////////option请求//////////////////////////////////////////////////

    public Response option(String url) {
        Response response = new Response();
        response = httpComponets.option(url);
        return response;
    }

    public Response option(String url, String charset) {
        Response response = new Response();
        response = httpComponets.option(url, charset);
        return response;
    }

    public Response option(String url, Map<String, String> headers) {
        Response response = new Response();
        response = httpComponets.option(url, headers);
        return response;
    }

    public Response option(String url, Map<String, String> headers, String charset) {
        Response response = new Response();
        response = httpComponets.option(url, headers, charset);
        return response;
    }

    public Response option(String url, String charset, HttpHost proxy) {
        Response response = new Response();
        response = httpComponets.option(url, charset, proxy);
        return response;
    }

    public Response option(String url, Map<String, String> headers, String charset, HttpHost proxy) {
        Response response = new Response();
        response = httpComponets.option(url, headers, charset, proxy);
        return response;
    }

/////////////////////////////////////////////getImage请求//////////////////////////////////////////////////

    public Response getImage(String url) {
        if (url == null)
            return null;
        else {
            return httpComponets.getImage(url);
        }
    }

    public Response getImage(String url, Map<String, String> headers) {
        if (url == null)
            return null;
        else {
            return httpComponets.getImage(url,headers);
        }
    }

    public Response getImage(String url, HttpHost host) {
        if (url == null)
            return null;
        else {
            return httpComponets.getImage(url,host);
        }
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 关闭lc
     */
    public void close() {
        httpComponets.close();
    }

    /**
     * @return lc连接是否关闭
     */
    public Boolean isOpen() {
        return httpComponets.isOpen();
    }

    /**
     * @return 默认编码
     */
    public String get_DEFLAUT_CHARSET() {
        return httpComponets.get_DEFLAUT_CHARSET();
    }

    public HttpComponentsUtil getHttpComponets() {
        return httpComponets;
    }

    public void setHttpComponets(HttpComponentsUtil httpComponets) {
        this.httpComponets = httpComponets;
    }

    /**
     * 添加BasicClientCookie类型cookie
     * @param cookie
     */
    public void addCookies( BasicClientCookie cookie){
        httpComponets.addCookies(cookie);
    }

    /**
     * 添加key-value类型cookie
     * @param key
     * @param vaule
     * @param domain
     * @param path
     */
    public void addCookies( String key,String vaule,String domain,String path){
        httpComponets.addCookies( key,vaule,domain,path);
    }

    /**
     * 打印当前cookies，方便调试
     */
    public void printCookies() {
        httpComponets.printCookies();
    }

    /**
     * 设置cookiestore
     * @param cookieStore
     */
    public void setCookieStore(BasicCookieStore cookieStore) {
        httpComponets.setCookieStore(cookieStore);
    }

    /**
     * @return 当前的cookieStore
     */
    public BasicCookieStore getCookieStore() {
        return httpComponets.getCookieStore();
    }

    /**
     * 设置代理
     * @param proxy
     */
    public void setProxy(HttpHost proxy){
        httpComponets.setProxy(proxy);
    }

    /**
     * @return 当前使用的代理
     */
    public HttpHost getProxy() {
        return httpComponets.getProxy();
    }

    /**
     * 设置认证
     * @param userName
     * @param password
     */
    public void setCredentials(String userName, String password) {
        httpComponets.setCredentials(userName,password);
    }

    /**
     * 同时设置代理与认证
     * @param host
     * @param userName
     * @param password
     */
    public void setProxyAndCredentials(HttpHost host, String userName, String password) {
        httpComponets.setProxyAndCredentials(host, userName, password);
    }

    /**
     * 设置统一超时时间
     * @param millsSeconds
     */
    public void setTimeout(int millsSeconds){
        httpComponets.setTimeout(millsSeconds);
    }

    /**
     * @return 统一超时时间
     */
    public int getTimeout() {
        return httpComponets.getTimeout();
    }

    /**
     * 设置连接超时及从连接池获取连接实例的超时
     * @param millsSeconds
     */
    public void setConnectionTimeout(int millsSeconds){
        httpComponets.setConnectionTimeout(millsSeconds);
    }

    /**
     * @return connection超时时间
     */
    public int getConnectionTimeout() {
        return httpComponets.getConnectionTimeout();
    }

    /**
     * 设置读取超时
     * @param millsSeconds
     */
    public void setSocketTimeout(int millsSeconds){
        httpComponets.setSocketTimeout(millsSeconds);
    }

    /**
     * @return 读取超时时间
     */
    public int getSocketTimeout() {
        return httpComponets.getSocketTimeout();
    }

    /**
     * @return 最大连接数
     */
    public int getMaxTotalConnections() {
        return httpComponets.getMaxTotalConnections();
    }

    /**
     * 设置最大连接数
     * @param maxTotalConnections
     */
    public void setMaxTotalConnections(int maxTotalConnections) {
        httpComponets.setMaxTotalConnections(maxTotalConnections);
    }

    /**
     * @return 当前单个路由允许的最大连接
     */
    public int getMaxConnectionPerHost() {
        return httpComponets.getMaxConnectionPerHost();
    }

    /**
     * 设置单个路由允许的最大连接
     * @param maxConnectionPerHost
     */
    public void setMaxConnectionPerHost(int maxConnectionPerHost) {
        httpComponets.setMaxConnectionPerHost(maxConnectionPerHost);
    }

    /**
     * @return 最大重试次数
     */
    public int getMaxTryTimesToFetch() {
        return httpComponets.getMaxTryTimesToFetch();
    }

    /**
     * 设置最大重试次数
     * @param maxTryTimesToFetch
     */
    public void setMaxTryTimesToFetch(int maxTryTimesToFetch) {
        httpComponets.setMaxTryTimesToFetch(maxTryTimesToFetch);
    }

    /**
     * @return 当前是否允许重定向
     */
    public boolean getIsFollowRedirects() {
        return httpComponets.getisFollowRedirects();
    }

    /**
     * 设置是否允许重定向
     * @param followRedirects true or false
     */
    public void setIsFollowRedirects(boolean followRedirects) {
        httpComponets.setisFollowRedirects(followRedirects);
    }
}