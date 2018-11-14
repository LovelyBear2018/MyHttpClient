package com.crawler.artifact.mapping;

import com.crawler.artifact.http.environment.RetryEntity;
import com.crawler.artifact.type.HttpMethodType;
import com.crawler.artifact.session.Configuration;

import java.util.*;

/**
 * Created by liuzhixiong on 2018/9/17.
 * 映射的语句
 */

public final class MappedStatement {

    private String resource; //mapper.xml路径
    private Configuration configuration;
    private String id; //ID: 包名+类名+方法名
    private String url; //请求URL
    private HttpMethodType httpMethodType; //请求类型
    private Map<String, String> headers; //单个请求请求头
    private Map<String, String> params; //请求数据
    private RetryEntity retryEntity; //重试参数
    private String charset; //编码类型
    private Properties variables; //其他请求参数
    private boolean resume; //单次请求之后是否恢复全局配置

    public MappedStatement() {
        retryEntity = new RetryEntity();
    }

    //静态内部类，建造者模式
    public static class Builder {
        private MappedStatement mappedStatement = new MappedStatement();

        public Builder(Configuration configuration, String id, String url, HttpMethodType httpMethodType) {
            mappedStatement.configuration = configuration;
            mappedStatement.id = id;
            mappedStatement.url = url;
            mappedStatement.httpMethodType = httpMethodType;
        }

        public Builder resource(String resource) {
            mappedStatement.resource = resource;
            return this;
        }

        public Builder headers(Map<String, String> headers) {
            mappedStatement.headers = headers;
            return this;
        }

        public Builder params(Map<String, String> params) {
            mappedStatement.params = params;
            return this;
        }

        public Builder retryEntity(RetryEntity retryEntity){
            mappedStatement.retryEntity = retryEntity;
            return this;
        }

        public Builder charset(String charset) {
            mappedStatement.charset = charset;
            return this;
        }

        public Builder variables(Properties variables) {
            mappedStatement.variables = variables;
            return this;
        }

        public Builder resume(boolean resume) {
            mappedStatement.resume = resume;
            return this;
        }

        public String id() {
            return mappedStatement.id;
        }

        public MappedStatement build() {
            assert mappedStatement.configuration != null;
            assert mappedStatement.id != null;
            assert mappedStatement.httpMethodType != null;
            return mappedStatement;
        }
    }

    public boolean isResume() {
        return resume;
    }

    public void setResume(boolean resume) {
        this.resume = resume;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setHttpMethodType(HttpMethodType httpMethodType) {
        this.httpMethodType = httpMethodType;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public RetryEntity getRetryEntity() {
        return retryEntity;
    }

    public void setRetryEntity(RetryEntity retryEntity) {
        this.retryEntity = retryEntity;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public void setVariables(Properties variables) {
        this.variables = variables;
    }

    public HttpMethodType getHttpMethodType() {
        return httpMethodType;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Properties getVariables() {
        return variables;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getResource() {
        return resource;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    @Override
    public String toString() {
        return "MappedStatement{" +
                "resource='" + resource + '\'' +
                ", id='" + id + '\'' +
                ", url='" + url + '\'' +
                ", httpMethodType=" + httpMethodType +
                ", headers=" + headers +
                ", params=" + params +
                ", retryEntity=" + retryEntity +
                ", charset='" + charset + '\'' +
                ", variables=" + variables +
                '}';
    }
}
