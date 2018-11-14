package com.crawler.artifact.binding;

import java.lang.reflect.Proxy;

import com.crawler.artifact.session.CrawlerSession;

/**
 * Created by liuzhixiong on 2018/9/17.
 * MapperProxy工厂类
 */
public class MapperProxyFactory<T> {

    private final Class<T> mapperInterface; //mapper类字节码

    /**
     * 初始化代理工厂
     * @param mapperInterface
     */
    public MapperProxyFactory(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    public Class<T> getMapperInterface() {
        return mapperInterface;
    }

    /**
     * 使用JDK动态代理生成代理类
     * @param mapperProxy
     * @return
     */
    @SuppressWarnings("unchecked")
    protected T newInstance(MapperProxy<T> mapperProxy) {
        return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[] { mapperInterface }, mapperProxy);
    }

    /**
     * 生成Invocation类
     * @param crawlerSession
     * @return
     */
    public T newInstance(CrawlerSession crawlerSession) {
        final MapperProxy<T> mapperProxy = new MapperProxy<T>(crawlerSession, mapperInterface);
        return newInstance(mapperProxy);
    }

}
