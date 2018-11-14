package com.crawler.artifact.session.defaults;

import com.crawler.artifact.session.Configuration;
import com.crawler.artifact.session.CrawlerSessionFactory;
import com.crawler.artifact.session.CrawlerSession;

/**
 * Created by liuzhixiong on 2018/9/17.
 * 默认会话工厂
 */

public class DefaultCrawlerSessionFactory implements CrawlerSessionFactory {

    private final Configuration configuration;

    public DefaultCrawlerSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public CrawlerSession openSession() {
        return new DefaultCrawlerSession(configuration);
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

}