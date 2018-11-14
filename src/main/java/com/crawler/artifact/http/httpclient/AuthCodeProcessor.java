package com.crawler.artifact.http.httpclient;

import com.crawler.artifact.binding.AuthCodeCacheBean;

/**
 * Created by liuzhixiong on 2018/10/22.
 */
public interface AuthCodeProcessor {

    AuthCodeCacheBean buildAuthCodeCacheBean();

}
