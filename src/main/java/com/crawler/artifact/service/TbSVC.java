package com.crawler.artifact.service;

import com.crawler.artifact.annocation.Proxy;
import com.crawler.artifact.mappers.TbMapper;
import org.apache.http.HttpHost;

import javax.annotation.Resource;

/**
 * Created by liuzhixiong on 2018/9/20.
 */
public class TbSVC {

    @Resource
    TbMapper tbMapper;

    @Proxy
    public CrawlerResult login(String sessionId){

        CrawlerResult crawlerResult = new CrawlerResult();
        crawlerResult.setResultCode(1);
        crawlerResult.setProxy(new HttpHost("117.78.52.9", 8080));

        return crawlerResult;
    }

}
