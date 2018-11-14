package com.crawler.artifact.http.environment;

import org.apache.http.impl.cookie.BasicClientCookie;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by liuzhixiong on 2018/9/26.
 * HttpClient  Cookie封装类
 */

public abstract class ClientCookie {

    /**
     * 添加Cookie
     * @param name
     * @param value
     */
    protected abstract void addCookie(String name, String value);

    /**
     * 移除Cookie
     * @param name
     * @return
     */
    protected abstract boolean removeCookie(String name);

    /**
     * 添加Cookie
     * @param name
     * @param value
     * @param domain
     * @param path
     */
    protected abstract void addCookie(String name, String value, String domain, String path);

}
