package com.crawler.artifact.beans;

import java.lang.reflect.Proxy;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationHandler;

import com.crawler.artifact.session.Configuration;
import com.crawler.artifact.session.CrawlerSessionFactory;
import com.crawler.artifact.session.CrawlerSession;

/**
 * Created by liuzhixiong on 2018/9/25.
 */
public class CrawlerSessionTemplate implements CrawlerSession {

    private final CrawlerSessionFactory crawlerSessionFactory;
    private final CrawlerSession crawlerSessionProxy;

    public CrawlerSessionTemplate(CrawlerSessionFactory crawlerSessionFactory) {
        this.crawlerSessionFactory = crawlerSessionFactory;
        this.crawlerSessionProxy = (CrawlerSession) Proxy.newProxyInstance(
                CrawlerSessionFactory.class.getClassLoader(),
                new Class[] { CrawlerSession.class },
                new CrawlerSessionInterceptor());
    }

    public static CrawlerSession getCrawlerSession(CrawlerSessionFactory crawlerSessionFactory){
         return crawlerSessionFactory.openSession();
    }

    @Override
    public <T> T getMapper(Class<T> type) {
        return getConfiguration().getMapper(type, this);
    }

    @Override
    public Configuration getConfiguration() {
        return this.crawlerSessionFactory.getConfiguration();
    }

    private class CrawlerSessionInterceptor implements InvocationHandler {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            CrawlerSession crawlerSession = getCrawlerSession(CrawlerSessionTemplate.this.crawlerSessionFactory);
            Object result = method.invoke(crawlerSession, args);
            return result;
        }
    }

}
