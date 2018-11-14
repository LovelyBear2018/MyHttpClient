package com.crawler.artifact.builder;

/**
 * Created by liuzhixiong on 2018/9/26.
 * Mapper全局代理处理类
 */

public class GlobalProxyResolver {

    private final MapperBuilderAssistant assistant;
    private String serviceType;
    private String fcType;
    private String strategy;


    public GlobalProxyResolver(MapperBuilderAssistant assistant, String serviceType, String fcType, String strategy) {
        this.assistant = assistant;
        this.serviceType = serviceType;
        this.fcType = fcType;
        this.strategy = strategy;
    }

    /**
     * 调用MapperBuilderAssistant添加全局代理
     */
    public void resolve() {
        assistant.addGlobalProxyType(this.serviceType, this.fcType, this.strategy);
    }

}
