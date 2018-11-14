package com.crawler.artifact.beans;

import com.crawler.artifact.session.CrawlerSession;
import com.crawler.artifact.session.CrawlerSessionFactory;

/**
 * Created by liuzhixiong on 2018/9/25.
 */
public class CrawlerSessionSupport {

    private CrawlerSession crawlerSession;
    private CrawlerSessionFactory crawlerSessionFactory;

    public void setCrawlerSessionFactory(CrawlerSessionFactory crawlerSessionFactory) {
        this.crawlerSessionFactory = crawlerSessionFactory;
        this.crawlerSession = new CrawlerSessionTemplate(crawlerSessionFactory);
    }

    public CrawlerSession getCrawlerSession() {
        return this.crawlerSession;
    }

}
