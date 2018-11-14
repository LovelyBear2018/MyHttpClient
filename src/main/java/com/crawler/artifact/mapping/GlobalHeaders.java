package com.crawler.artifact.mapping;

import com.crawler.artifact.session.Configuration;

import java.util.Map;

/**
 * Created by liuzhixiong on 2018/9/26.
 * 全局请求头封装类
 */

public class GlobalHeaders {

    private Configuration configuration;
    private String id; //全局请求头对应ID,格式为  包名+Mapper类名+headers标签的ID
    private Map<String, String> globalHeader; //全局请求头

    /**
     * 保证不可实例化
     */
    private GlobalHeaders() {
    }

    public static class Builder {

        private GlobalHeaders globalHeaders = new GlobalHeaders();

        public Builder(Configuration configuration, String id, Map<String, String> globalHeader) {
            globalHeaders.configuration = configuration;
            globalHeaders.id = id;
            globalHeaders.globalHeader = globalHeader;
        }

        public GlobalHeaders build() {
            assert globalHeaders.configuration != null;
            assert globalHeaders.id != null;
            assert globalHeaders.getGlobalHeader() != null;
            return globalHeaders;
        }

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public Map<String, String> getGlobalHeader() {
        return globalHeader;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public void setGlobalHeader(Map<String, String> globalHeader) {
        this.globalHeader = globalHeader;
    }

    @Override
    public String toString() {
        return "GlobalHeaders{" +
                "id='" + id + '\'' +
                ", globalHeader=" + globalHeader +
                '}';
    }
}
