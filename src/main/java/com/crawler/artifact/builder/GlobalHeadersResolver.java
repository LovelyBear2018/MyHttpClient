package com.crawler.artifact.builder;

import java.util.Map;

/**
 * Created by liuzhixiong on 2018/9/26.
 * 全局请求头处理类
 */
public class GlobalHeadersResolver {

    private final MapperBuilderAssistant assistant;
    private String id;
    private Map<String, String> globalHeaders;


    public GlobalHeadersResolver(MapperBuilderAssistant assistant, String id, Map<String, String> globalHeaders) {
        this.assistant = assistant;
        this.id = id;
        this.globalHeaders = globalHeaders;
    }

    /**
     * 调用MapperBuilderAssistant添加全局请求头
     */
    public void resolve() {
        assistant.addGlobalHeaders(id, globalHeaders);
    }

}
