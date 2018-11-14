package com.crawler.artifact.mappers;

import com.crawler.artifact.annocation.RequestMethod;
import com.crawler.artifact.annocation.Start;
import com.crawler.artifact.annocation.Url;
import com.crawler.artifact.http.httpclient.Response;
import com.crawler.artifact.type.HttpMethodType;

/**
 * Created by liuzhixiong on 2018/9/17.
 */

public interface JdMapper {

    Response getAuthCode();

    @Start
    @Url("https://www.jd.com/")
    @RequestMethod(HttpMethodType.GET)
    Response login();

}
