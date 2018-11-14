package com.crawler.artifact.session;

/**
 * Created by liuzhixiong on 2018/9/17.
 */
public interface CrawlerSessionFactory {

    CrawlerSession openSession();
    Configuration getConfiguration();
}
