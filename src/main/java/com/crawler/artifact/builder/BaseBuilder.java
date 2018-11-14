package com.crawler.artifact.builder;

import com.crawler.artifact.mapping.GlobalHeaders;
import com.crawler.artifact.parsing.XNode;
import com.crawler.artifact.session.Configuration;

import org.apache.commons.lang3.StringUtils;

import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

/**
 * Created by liuzhixiong on 2018/9/17.
 */
public abstract class BaseBuilder {

    protected MapperBuilderAssistant builderAssistant;
    protected final Configuration configuration;

    public BaseBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    /**
     * 解析xml节点并构建Map
     * @param map
     * @param node
     */
    public void fillMap(Map<String, String> map, XNode node){
        if(node != null){

            String refid = node.getStringAttribute("refid");
            if(StringUtils.isNotBlank(refid)){
                refid = builderAssistant.applyCurrentNamespace(refid, false);
                GlobalHeaders globalHeaders = configuration.getGlobalHeaders(refid);
                Map<String, String> globalHeader = globalHeaders.getGlobalHeader();
                if (globalHeader != null) {
                    map.putAll(globalHeader);
                }
            }

            Properties defaults = node.getChildrenAsProperties();
            Enumeration<?> enu = defaults.propertyNames();
            while (enu.hasMoreElements()) {
                String key = (String)enu.nextElement();
                String val = (String)defaults.get(key);
                map.put(key, val);
            }
        }
    }
}
