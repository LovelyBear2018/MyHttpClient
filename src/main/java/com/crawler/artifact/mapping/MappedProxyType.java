package com.crawler.artifact.mapping;

import com.crawler.artifact.session.Configuration;

/**
 * Created by liuzhixiong on 2018/9/26.
 * 全局代理类型封装类
 */

public class MappedProxyType {

    private Configuration configuration;
    private String id;
    private String serviceType;
    private String fcType;
    private String strategy;

    public static class Builder {

        private MappedProxyType pappedProxyType = new MappedProxyType();

        public Builder(Configuration configuration, String id, String serviceType, String fcType, String strategy) {
            pappedProxyType.configuration = configuration;
            pappedProxyType.id = id;
            pappedProxyType.serviceType = serviceType;
            pappedProxyType.fcType = fcType;
            pappedProxyType.strategy = strategy;
        }

        public MappedProxyType build() {
            assert pappedProxyType.configuration != null;
            assert pappedProxyType.id != null;
            assert pappedProxyType.serviceType != null;
            return pappedProxyType;
        }

    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public String getId() {
        return id;
    }

    public String getServiceType() {
        return serviceType;
    }

    public String getFcType() {
        return fcType;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public void setFcType(String fcType) {
        this.fcType = fcType;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    @Override
    public String toString() {
        return "MappedProxyType{" +
                "id='" + id + '\'' +
                ", serviceType='" + serviceType + '\'' +
                ", fcType='" + fcType + '\'' +
                ", strategy='" + strategy + '\'' +
                '}';
    }
}
