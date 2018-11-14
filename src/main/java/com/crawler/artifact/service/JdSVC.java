package com.crawler.artifact.service;

import com.crawler.artifact.annocation.Proxy;
import com.crawler.artifact.annocation.Session;
import com.crawler.artifact.http.httpclient.Response;
import com.crawler.artifact.mappers.JdMapper;

import javax.annotation.Resource;

/**
 * Created by liuzhixiong on 2018/9/20.
 */
public class JdSVC {

    @Resource
    JdMapper jdMapper;

    @Session
    @Proxy
    public CrawlerResult jd_login(InfoBean InfoBean){

        Response response = jdMapper.login();

        System.out.println(response.getStatus().getStatecode());
        System.out.println(response.getContent());
        CrawlerResult crawlerResult = new CrawlerResult();
        crawlerResult.setResultCode(1);
        crawlerResult.setProxy(response.getProxy());

        return crawlerResult;
    }

}
