package com.crawler.artifact.builder.xml;

import com.crawler.artifact.exceptions.BuilderException;
import com.crawler.artifact.io.Resources;
import com.crawler.artifact.io.VFS;
import com.crawler.artifact.logging.LogFactory;
import com.crawler.artifact.node.Attribute;
import com.crawler.artifact.node.Element;
import com.crawler.artifact.parsing.XPathParser;
import com.crawler.artifact.session.Configuration;
import com.crawler.artifact.builder.BaseBuilder;
import com.crawler.artifact.parsing.XNode;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * Created by liuzhixiong on 2018/9/17.
 * config.xml文件解析类
 */

public class XMLConfigBuilder extends BaseBuilder {

    private boolean parsed;//是否已解析
    private XPathParser parser; //Xpath解析器

    /**
     * 初始化
     * @param inputStream
     * @param props
     */
    public XMLConfigBuilder(InputStream inputStream, Properties props) {
        this(new XPathParser(inputStream, true, props), props);
    }

    /**
     * 初始化
     * @param parser
     * @param props
     */
    private XMLConfigBuilder(XPathParser parser, Properties props) {
        super(new Configuration());
        this.configuration.setVariables(props);
        this.parsed = false;
        this.parser = parser;
    }

    /**
     * 解析configuration节点
     * @return
     */
    public Configuration parse() {
        if (parsed) {
            throw new BuilderException("Each XMLConfigBuilder can only be used once.");
        }
        parsed = true;

        parseConfiguration(parser.evalNode(Element.configuration.name()));

        return configuration;
    }

    /**
     * 解析configuration节点
     * @return
     */
    private void parseConfiguration(XNode root) {
        LogFactory.logInfo("parsing crawler-config.xml <configuration> node.");
        try {
            settingsElement(root.evalNode(Element.settings.name()));
            propertiesElement(root.evalNode(Element.properties.name()));
            mapperElement(root.evalNode(Element.mappers.name()));
        } catch (Exception e) {
            throw new BuilderException("Error parsing crawler-config.xml <configuration>. Cause: " + e, e);
        }
    }

    /**
     * 解析settings节点
     * @return
     */
    private void settingsElement(XNode context) {
        LogFactory.logInfo("parsing crawler-config.xml <settings> node.");
        if (context  != null) {
            List<XNode> XNodes = context.evalNodes(Element.listSettingsElement());
            for(XNode node:XNodes){
                registerSetting(node);
            }
        } else {
            LogFactory.logInfo("crawler-config.xml <settings> node is null.");
        }
    }

    /**
     * 解析property节点
     * @return
     */
    private void propertiesElement(XNode context) throws Exception {
        LogFactory.logInfo("parsing crawler-config.xml <properties> node.");
        if (context != null) {
            Properties defaults = context.getChildrenAsProperties();
            Properties vars = configuration.getVariables();
            if (vars != null) {
                defaults.putAll(vars);
            }
            parser.setVariables(defaults);
            configuration.setVariables(defaults);

            LogFactory.logInfo("parsing crawler-config.xml <properties> node properties=" + defaults.toString() + ".");
        } else {
            LogFactory.logInfo("crawler-config.xml <properties> node is null.");
        }
    }

    /**
     * 初始化setting配置信息
     * @param node
     */
    private void registerSetting(XNode node) {
        if(node != null){
            Element nodeType = Element.match(node.getName());
            if(nodeType !=  null){
                if(nodeType == Element.log){
                    initLogUtil(node);
                } else if(nodeType == Element.cache) {
                    initCacheInfo(node);
                }
            } else {
                LogFactory.logInfo("registerSetting()---nodeType = null, nodename=" + node.getName() + ".");
            }
        } else {
            LogFactory.logInfo("registerSetting()---node = null.");
        }
    }

    /**
     * 初始化日志工具
     * @param node
     */
    private void initLogUtil(XNode node) {
        String type = node.getStringAttribute(Attribute.type.name());
        String level = node.getStringAttribute(Attribute.level.name());
        LogFactory.createLog(type, level);

        LogFactory.logInfo("parsing crawler-config.xml <log> node type=" + type + ",level=" + level + ".");
    }

    /**
     * 初始化缓存配置
     * @param node
     */
    private void initCacheInfo(XNode node) {

        String timeout = node.getStringAttribute(Attribute.timeout.name());
        configuration.setGlobalCacheTimeout(Long.valueOf(timeout));

        LogFactory.logInfo("parsing crawler-config.xml <cache> node timeout=" + timeout + " seconds.");
    }

    /**
     * 初始化CAT监控配置
     * @param node
     */
    private void initCatInfo(XNode node) {
        if(node != null) {
            configuration.setCat(true);
        }

        LogFactory.logInfo("parsing crawler-config.xml <cat> node node=" + node + ".");
    }

    /**
     * 解析mappers节点
     * @return
     */
    private void mapperElement(XNode parent) throws Exception {
        LogFactory.logInfo("parsing crawler-config.xml <mappers> node.");
        if (parent != null) {
            for (XNode child : parent.getChildren()) {
                if (Element.pack.name().equals(child.getName())) {
                    String mapperPackage = child.getStringAttribute(Attribute.value.name());
                    List<String> children = VFS.getInstance().list(mapperPackage);

                    LogFactory.logInfo("crawler-config.xml <mappers-package> node packageName=" + mapperPackage +
                            ",mappers=" + children + ".");
                    for (String oneChild:children) {
                        singleMappperElement(oneChild);
                    }
                } else {
                    String resource = child.getStringAttribute(Attribute.resource.name());
                    LogFactory.logInfo("crawler-config.xml <mappers-mapper> node resource=" + resource + ".");
                    singleMappperElement(resource);
                }
            }
        } else {
            LogFactory.logInfo("crawler-config.xml <mappers> node is null.");
        }
    }

    /**
     * 解析单个Mapper.xml
     * @param resource
     */
    private void singleMappperElement(String resource){
        try {
            InputStream inputStream = Resources.getResourceAsStream(resource);
            XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream, configuration, resource);
            mapperParser.parse();
        } catch (IOException e) {
            throw new BuilderException("Error parsing Crawler Mapper Configuration. Cause: " + e, e);
        }
    }
}
