package com.crawler.artifact.http.environment;

import org.apache.http.impl.cookie.BasicClientCookie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuzhixiong on 2018/10/22.
 * LoginCrawler的Cookie类
 */
public class LoginCrawlerCookie extends ClientCookie{

    private List<BasicClientCookie> cookies = new ArrayList<BasicClientCookie>();

    @Override
    public void addCookie(String name, String value){
        BasicClientCookie cookie = new BasicClientCookie(name, value);
        cookies.add(cookie);
    }

    @Override
    public boolean removeCookie(String name){
        for(BasicClientCookie cookie:cookies){
            if(cookie.getName().equals(name)){
                cookies.remove(cookie);
                return true;
            }
        }
        return false;
    }

    @Override
    public void addCookie(String name, String value, String domain, String path){
        BasicClientCookie cookie = new BasicClientCookie(name, value);
        cookie.setDomain(domain);
        cookie.setPath(path);
        cookies.add(cookie);
    }

    public List<BasicClientCookie> getCookies() {
        return cookies;
    }

}
