package com.crawler.artifact.session;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.crawler.artifact.builder.xml.XMLConfigBuilder;
import com.crawler.artifact.exceptions.ExceptionFactory;
import com.crawler.artifact.session.defaults.DefaultCrawlerSessionFactory;

/**
 * Created by liuzhixiong on 2018/9/17.
 */

public class CrawlerSessionFactoryBuilder {

    public CrawlerSessionFactory build(InputStream inputStream){ return build(inputStream, null); }

    /**
     * 使用参照XML文档或更特定的CrawlrMapConfig.xml文件的InputStream实例,properties为可选参数
     * @param inputStream
     * @param properties
     * @return
     */
    public CrawlerSessionFactory build(InputStream inputStream, Properties properties){

        try{
            XMLConfigBuilder parser = new XMLConfigBuilder(inputStream, properties);
            return build(parser.parse());
        } catch (Exception e) {
            throw ExceptionFactory.wrapException("Error building CrawlerSessionFactory.", e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 生成SessionFactory实例
     * @param config
     * @return
     */
    public CrawlerSessionFactory build(Configuration config) {
        return new DefaultCrawlerSessionFactory(config);
    }

}
