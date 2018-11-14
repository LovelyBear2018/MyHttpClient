package com.crawler.artifact.binding;

import java.util.*;

import com.crawler.artifact.exceptions.BindingException;
import com.crawler.artifact.io.ResolverUtil;
import com.crawler.artifact.session.Configuration;
import com.crawler.artifact.session.CrawlerSession;

/**
 * Created by liuzhixiong on 2018/9/17.
 * Mapper注册器
 */

public class MapperRegistry {

    private Configuration config;

    private final Map<Class<?>, MapperProxyFactory<?>> knownMappers = new HashMap<Class<?>, MapperProxyFactory<?>>(); //已注册代理工厂类

    public MapperRegistry(Configuration config) {
        this.config = config;
    }
    public <T> boolean hasMapper(Class<T> type) {
        return knownMappers.containsKey(type);
    }

    /**
     * 按照包添加Mapper
     * @param packageName
     * @param superType
     */
    public void addMappers(String packageName, Class<?> superType) {
        //查找包下所有是superType的类
        ResolverUtil<Class<?>> resolverUtil = new ResolverUtil<Class<?>>();
        resolverUtil.find(new ResolverUtil.IsA(superType), packageName);
        Set<Class<? extends Class<?>>> mapperSet = resolverUtil.getClasses();
        for (Class<?> mapperClass : mapperSet) {
            addMapper(mapperClass);
        }
    }

    /**
     * 添加Mapper
     * @param type
     * @param <T>
     */
    public <T> void addMapper(Class<T> type) {
        //mapper必须是接口！才会添加
        if (type.isInterface()) {
            if (hasMapper(type)) {
                //如果重复添加了，报错
                throw new BindingException("Type " + type + " is already known to the MapperRegistry.");
            }
            boolean loadCompleted = false;
            try {
                knownMappers.put(type, new MapperProxyFactory<T>(type));
                loadCompleted = true;
            } finally {
                //如果加载过程中出现异常需要再将这个mapper从mybatis中删除
                if (!loadCompleted) {
                    knownMappers.remove(type);
                }
            }
        }
    }

    /**
     * 根据包名注册mapper
     * @param packageName
     */
    public void addMappers(String packageName) {
        addMappers(packageName, Object.class);
    }

    /**
     * 使用代理工厂类获取Mapper
     * @param type
     * @param crawlerSession
     * @param <T>
     * @return
     */
    public <T> T getMapper(Class<T> type, CrawlerSession crawlerSession) {
        final MapperProxyFactory<T> mapperProxyFactory = (MapperProxyFactory<T>) knownMappers.get(type);
        if (mapperProxyFactory == null) {
            throw new BindingException("Type " + type + " is not known to the MapperRegistry.");
        }
        try {
            return mapperProxyFactory.newInstance(crawlerSession);
        } catch (Exception e) {
            throw new BindingException("Error getting mapper instance. Cause: " + e, e);
        }
    }

}
