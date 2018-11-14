package com.crawler.artifact.builder;

import java.util.*;

import com.crawler.artifact.type.CrawlerType;
import com.crawler.artifact.http.environment.RetryEntity;
import com.crawler.artifact.mapping.GlobalHeaders;
import com.crawler.artifact.mapping.MappedProxyType;
import com.crawler.artifact.type.HttpMethodType;
import com.crawler.artifact.session.Configuration;
import com.crawler.artifact.exceptions.BuilderException;
import com.crawler.artifact.mapping.MappedStatement;

/**
 * Created by liuzhixiong on 2018/9/17.
 * 映射构建器助手，建造者模式,继承BaseBuilder
 */
public class MapperBuilderAssistant extends BaseBuilder {

    //每个助手都有1个namespace,resource
    private String currentNamespace;
    private String resource;

    public MapperBuilderAssistant(Configuration configuration, String resource) {
        super(configuration);
        this.resource = resource;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getCurrentNamespace() {
        return currentNamespace;
    }

    public void setCurrentNamespace(String currentNamespace) {
        if (currentNamespace == null) {
            throw new BuilderException("The mapper element requires a namespace attribute to be specified.");
        }

        if (this.currentNamespace != null && !this.currentNamespace.equals(currentNamespace)) {
            throw new BuilderException("Wrong namespace. Expected '"
                    + this.currentNamespace + "' but found '" + currentNamespace + "'.");
        }

        this.currentNamespace = currentNamespace;
    }

    /**
     * 为id加上namespace前缀，如ffpLogin-->org.a.b.ffpLogin
     * @param base
     * @param isReference
     * @return
     */
    public String applyCurrentNamespace(String base, boolean isReference) {
        if (base == null) {
            return null;
        }
        if (isReference) {
            // is it qualified with any namespace yet?
            if (base.contains(".")) {
                return base;
            }
        } else {
            // is it qualified with this namespace yet?
            if (base.startsWith(currentNamespace + ".")) {
                return base;
            }
            if (base.contains(".")) {
                throw new BuilderException("Dots are not allowed in element names, please remove it from " + base);
            }
        }
        return currentNamespace + "." + base;
    }

    /**
     * 添加statement
     * @param id
     * @param url
     * @param httpMethodType
     * @param headers
     * @param params
     * @param retryEntity
     * @param charset
     * @param properties
     * @return
     */
    public MappedStatement addMappedStatement(
            String id,
            String url,
            HttpMethodType httpMethodType,
            Map<String, String> headers,
            Map<String, String> params,
            RetryEntity retryEntity,
            String charset,
            Properties properties,
            boolean resume
                        ) {

        id = applyCurrentNamespace(id, false);

        MappedStatement.Builder statementBuilder = new MappedStatement.Builder(configuration, id, url, httpMethodType);
        statementBuilder.resource(resource).headers(headers).params(params).retryEntity(retryEntity).charset(charset)
                .variables(properties).resume(resume);
        MappedStatement statement = statementBuilder.build();

        configuration.addMappedStatement(statement);
        return statement;
    }

    /**
     * 添加Mapper全局请求头
     * @param id
     * @param globalHeader
     */
    public void addGlobalHeaders(String id, Map<String, String> globalHeader) {
        id = applyCurrentNamespace(id, false);

        GlobalHeaders.Builder globalBuilder = new GlobalHeaders.Builder(configuration, id, globalHeader);
        GlobalHeaders globalHeaders = globalBuilder.build();

        configuration.addGlobalHeaders(globalHeaders);
    }

    /**
     * 添加Mapper缓存全局超时时间
     * @param timeout
     */
    public void addMapperCacheTimeout(String timeout) {
        String id = this.currentNamespace;
        configuration.addMapperCacheByTimeout(id, Long.valueOf(timeout));
    }

    /**
     * 添加Mapper缓存全局编码
     * @param charset
     */
    public void addGlobalCharSet(String charset) {
        String id = this.currentNamespace;
        configuration.addGlobalCharset(id, charset);
    }

    /**
     * 添加Mapper缓存全局抓取器类型
     * @param crawlerType
     */
    public void addGlobalCrawlerType(CrawlerType crawlerType){
        String id = this.currentNamespace;
        configuration.addGlobalCrawlerType(id, crawlerType);
    }

    /**
     * 添加Mapper缓存全局代理类型
     * @param serviceType
     * @param fcType
     * @param strategy
     */
    public void addGlobalProxyType(String serviceType, String fcType, String strategy) {
        String id = this.currentNamespace;

        MappedProxyType.Builder proxyTypeBuilder = new MappedProxyType.Builder(configuration, id, serviceType, fcType, strategy);
        MappedProxyType mappedProxyType = proxyTypeBuilder.build();

        configuration.addGlobalProxyType(mappedProxyType);
    }

    @Override
    public String toString() {
        return "MapperBuilderAssistant{" +
                "currentNamespace='" + currentNamespace + '\'' +
                ", resource='" + resource + '\'' +
                '}';
    }
}
