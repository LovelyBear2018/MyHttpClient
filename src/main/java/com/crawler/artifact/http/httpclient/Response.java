package com.crawler.artifact.http.httpclient;

import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;

/**
 * Created by liuzhixiong on 2018/11/14.
 */

public class Response {
    HttpHost proxy;
    String content;
    BasicCookieStore cookieStore;
    String location;
    byte[] bufferedImage;
    Header[] headers;
    Status status;
    Map<String,String> parameter;
    List<Cookie> cookie;


    public void setHeaders(Header[] headers) {
        this.headers = headers;
    }

    /**
     * @return 请求头
     */
    public Header[] getHeaders() {
        return headers;
    }


    public void setParameter(Map<String, String> parameter) {
        this.parameter = parameter;
    }

    /**
     * @return parameter参数
     */
    public Map<String, String> getParameter() {
        return parameter;
    }

    public List<Cookie> getCookie() {
        return cookie;
    }

    public void setCookie(List<Cookie> cookie) {
        this.cookie = cookie;
    }

    /**
     * @return 图片的BufferedImage数组
     */
    public byte[] getBufferedImage() {
        return bufferedImage;
    }

    public void setBufferedImage(byte[] bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    /**
     * @return 网页内容
     */
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return location
     */
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return 本次请求的CookieStore
     */
    public BasicCookieStore getCookieStore() {
        return cookieStore;
    }

    public void setCookieStore(BasicCookieStore cookieStore) {
        this.cookieStore = cookieStore;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * @return response状态，包括状态码和描述
     */
    public Status getStatus() {
        return status;
    }

    /**
     * @return 本次请求携带的proxy
     */
    public HttpHost getProxy() {
        return proxy;
    }

    public void setProxy(HttpHost host) {
        this.proxy = host;
    }

    public Header[] getResponseHeaders() {
        return headers;
    }

    public void setResponseHeaders(Header[] headers) {
        this.headers = headers;
    }

    /**
     * 打印本次请求保存的cookie
     */
    public void printCookies() {
        System.out.println("---查看当前Cookie---");
        List<Cookie> cookies = cookieStore.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                System.out.println(c.getName() + " : " + c.getValue());
                System.out.println("domain : " + c.getDomain());
                System.out.println("expires : " + c.getExpiryDate());
                System.out.println("path : " + c.getPath());
                System.out.println("version : " + c.getVersion());
            }
        } else {
            System.out.println("Cookie为空");
        }
    }

    /**
     * 打印本次请求携带的proxy
     */
    public void printProxy() {
        System.out.println("---查看当前proxy---");
        if (proxy != null) {
            System.out.println(proxy);
        } else {
            System.out.println("proxy为空");
        }
    }

    public void printResponseHeaders(){
        System.out.println("---查看当前返回Headers---");
        if (headers != null) {
            for (Header header : headers) {
                System.out.println(header.getName() + " : " + header.getValue());
            }
        }
    }
}
