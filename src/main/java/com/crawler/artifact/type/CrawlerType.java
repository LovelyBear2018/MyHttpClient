package com.crawler.artifact.type;

/**
 * Created by liuzhixiong on 2018/10/15.
 */
public enum CrawlerType {

    HTTPCLIENT,HTMLUNIT,SELENIUM;

    public static CrawlerType match(String crawlerTypeStr){
        for(CrawlerType crawlerType:CrawlerType.values()){
            if(crawlerType.name().equalsIgnoreCase(crawlerTypeStr)){
                return crawlerType;
            }
        }
        return null;
    }



}
