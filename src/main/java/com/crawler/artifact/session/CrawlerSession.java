package com.crawler.artifact.session;

/**
 * Created by liuzhixiong on 2018/9/17.
 */
public interface CrawlerSession {

    /**
     * Retrieves a mapper.
     * 得到映射器
     * 这个巧妙的使用了泛型，使得类型安全
     */
    <T> T getMapper(Class<T> type);

    Configuration getConfiguration();
}
