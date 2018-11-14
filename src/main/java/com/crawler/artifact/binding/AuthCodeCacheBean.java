package com.crawler.artifact.binding;

import com.crawler.artifact.http.httpclient.HttpCrawler;
import org.apache.http.HttpHost;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * Created by liuzhixiong on 2018/10/22.
 */
public class AuthCodeCacheBean {

    HttpCrawler hc;
    byte[] image;
    Map<String, String> parameter;
    HttpHost proxy;
    private Date date;
    int state = 1;

    public HttpCrawler getHc() {
        return hc;
    }

    public void setHc(HttpCrawler hc) {
        this.hc = hc;
    }

    public byte[] getImage() {
        return image;
    }

    public HttpHost getProxy() {
        return proxy;
    }

    public Map<String, String> getParameter() {
        return parameter;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public void setProxy(HttpHost proxy) {
        this.proxy = proxy;
    }

    public void setParameter(Map<String, String> parameter) {
        this.parameter = parameter;
    }


    public void setDate(Date date) {
        this.date = date;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Date getDate() {

        return date;
    }

    @Override
    public String toString() {
        return "AuthCodeCacheBean{" +
                "hc=" + hc +
                ", image=" + Arrays.toString(image) +
                ", parameter=" + parameter +
                ", date=" + date +
                '}';
    }

}
