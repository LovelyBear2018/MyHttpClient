package com.crawler.artifact.beans;

import org.springframework.beans.factory.FactoryBean;

/**
 * Created by liuzhixiong on 2018/9/25.
 */
public class MapperFactoryBean<T> extends CrawlerSessionSupport implements FactoryBean<T> {

    private Class<T> mapperInterface;

    public MapperFactoryBean() {
        //intentionally empty
    }

    public MapperFactoryBean(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    @Override
    public T getObject() throws Exception {
        return getCrawlerSession().getMapper(this.mapperInterface);
    }

    @Override
    public Class<T> getObjectType() {
        return this.mapperInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setMapperInterface(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    public Class<T> getMapperInterface() {
        return mapperInterface;
    }
}
