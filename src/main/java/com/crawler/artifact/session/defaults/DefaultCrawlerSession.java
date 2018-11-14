package com.crawler.artifact.session.defaults;

import com.crawler.artifact.session.Configuration;
import com.crawler.artifact.session.CrawlerSession;

/**
 * Created by liuzhixiong on 2018/9/18.
 * 默认抓取会话
 */
public class DefaultCrawlerSession implements CrawlerSession {

    private Configuration configuration;

    public DefaultCrawlerSession(Configuration configuration){
        this.configuration = configuration;
    }

    /**
     * 最后会去调用MapperRegistry.getMapper
     * @param type
     * @param <T>
     * @return
     */
    @Override
    public <T> T getMapper(Class<T> type) {
        return configuration.<T>getMapper(type, this);
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

}
