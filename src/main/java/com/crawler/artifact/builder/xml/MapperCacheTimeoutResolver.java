package com.crawler.artifact.builder.xml;

import com.crawler.artifact.builder.MapperBuilderAssistant;

/**
 * Created by liuzhixiong on 2018/9/29.
 */

public class MapperCacheTimeoutResolver {
    private final MapperBuilderAssistant assistant;
    private String timeout;

    public MapperCacheTimeoutResolver(MapperBuilderAssistant assistant, String timeout) {
        this.assistant = assistant;
        this.timeout = timeout;
    }

    public void resolve() {
        assistant.addMapperCacheTimeout(this.timeout);
    }
}
