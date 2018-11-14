package com.crawler.artifact.builder.xml;

import java.io.InputStream;
import java.util.*;

import com.crawler.artifact.builder.GlobalHeadersResolver;
import com.crawler.artifact.builder.MapperBuilderAssistant;
import com.crawler.artifact.exceptions.BuilderException;
import com.crawler.artifact.exceptions.IncompleteElementException;
import com.crawler.artifact.io.Resources;
import com.crawler.artifact.logging.LogFactory;
import com.crawler.artifact.node.Attribute;
import com.crawler.artifact.node.Element;
import com.crawler.artifact.parsing.XPathParser;
import com.crawler.artifact.session.Configuration;
import com.crawler.artifact.type.CrawlerType;
import com.crawler.artifact.builder.BaseBuilder;
import com.crawler.artifact.parsing.XNode;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by liuzhixiong on 2018/9/17.
 * XML映射构建器，建造者模式,继承BaseBuilder
 */

public class XMLMapperBuilder extends BaseBuilder {

    private XPathParser parser;
    //映射器构建助手
    private MapperBuilderAssistant builderAssistant;
    private String resource;

    public XMLMapperBuilder(InputStream inputStream, Configuration configuration, String resource) {
        this(new XPathParser(inputStream, true, configuration.getVariables(), new XMLMapperEntityResolver()),
                configuration, resource);
    }

    private XMLMapperBuilder(XPathParser parser, Configuration configuration, String resource) {
        super(configuration);
        this.builderAssistant = new MapperBuilderAssistant(configuration, resource);
        this.parser = parser;
        this.resource = resource;
    }

    /**
     * 解析mapper.xml
     */
    public void parse() {
        //如果没有加载过再加载，防止重复加载
        if (!configuration.isResourceLoaded(resource)) {
            configurationElement(parser.evalNode(Element.mapper.name()));
            configuration.addLoadedResource(resource); //标记一下,已经加载过了
            bindMapperForNamespace();//绑定映射器到namespace
        } else {
            LogFactory.logInfo("mapper " + resource + " have been loaded.");
        }
    }

    /**
     * 解析mapper标签
     * @param context
     */
    private void configurationElement(XNode context) {
        LogFactory.logInfo("parsing " + resource + " <mapper> node.");
        try {
            String namespace = context.getStringAttribute(Attribute.namespace.toString());
            if (StringUtils.isBlank(namespace)) {
                throw new BuilderException(resource + "'s namespace cannot be empty");
            }
            LogFactory.logInfo(resource + " namespace=" + namespace + ".");
            builderAssistant.setCurrentNamespace(namespace);

            globalCrawlerType(context.getStringAttribute(Attribute.crawlertype.name()));
            globalHeadersElements(context.evalNodes(Element.globalheaders.name()));
            globalCharSetElements(context.evalNode(Element.charset.name()));
            settingsElements(context.evalNode(Element.settings.name()));

            buildStatementFromContext(context.evalNodes(Element.listRequestMethod()));
        } catch (Exception e) {
            throw new BuilderException("Error parsing Mapper XML. Cause: " + e, e);
        }
    }

    /**
     * 全局抓取工具类型
     * @param crawlerType
     */
    private void globalCrawlerType(String crawlerType) {
        LogFactory.logInfo("parsing " + resource + " <crawlerType> value.");
        if (crawlerType != null) {
            CrawlerType crawlerTypeEnum = CrawlerType.valueOf(crawlerType);

            LogFactory.logInfo("parsing " + resource + " <crawlerType> value crawlerType=" + crawlerType + ".");

            if(crawlerTypeEnum == null){
                crawlerTypeEnum = CrawlerType.HTTPCLIENT;
            }

            builderAssistant.addGlobalCrawlerType(crawlerTypeEnum);
        } else {
            LogFactory.logInfo(resource + " <crawlerType> node is null.");
        }
    }

    /**
     * 解析所有globalheaders节点
     * @param list
     * @throws Exception
     */
    private void globalHeadersElements(List<XNode> list) throws Exception {
        LogFactory.logInfo("parsing " + resource + " <globalheaders> node.");
        for (XNode globalHeadersNode : list) {
            try {
                globalHeadersElement(globalHeadersNode);
            } catch (IncompleteElementException e) {
                e.printStackTrace();
            }
        }
    }

    private void settingsElements(XNode node) throws Exception {
        if (node != null) {
            List<XNode> nodes = node.evalNodes("cache");
            for(XNode tempNode:nodes){
                if(tempNode.getName().equals("cache")){
                    globalCacheElements(tempNode);
                }
            }
        }
    }

    /**
     * 解析charset节点
     * @param node
     * @throws Exception
     */
    private void globalCharSetElements(XNode node) throws Exception {
        LogFactory.logInfo("parsing " + resource + " <charset> node.");
        if (node != null) {
            String charset = node.getStringBody();

            LogFactory.logInfo("parsing " + resource + " <charset> node charset=" + charset + ".");

            builderAssistant.addGlobalCharSet(charset);
        } else {
            LogFactory.logInfo(resource + " <charset> node is null.");
        }
    }

    /**
     * 解析mapper级别cache节点
     * @param node
     * @throws Exception
     */
    private void globalCacheElements(XNode node) throws Exception {
        LogFactory.logInfo("parsing " + resource + " <settings-cache> node.");
        if (node != null) {
            String timeout = node.getStringAttribute("timeout");

            LogFactory.logInfo(resource + " <settings-cache> node timeout=" + timeout + " seconds.");

            MapperCacheTimeoutResolver mapperCacheTimeoutResolver = new MapperCacheTimeoutResolver(builderAssistant, timeout);
            mapperCacheTimeoutResolver.resolve();
        } else {
            LogFactory.logInfo(resource + " <settings-cache> node is null.");
        }
    }

    /**
     * 解析单个globalheaders节点
     * @param globalHeadersNode
     * @throws Exception
     */
    private void globalHeadersElement(XNode globalHeadersNode) throws Exception {

        String id = globalHeadersNode.getStringAttribute("id");

        Map<String, String> globalHeaders = new HashMap<String, String>();
        fillMap(globalHeaders, globalHeadersNode);

        LogFactory.logInfo("parsing " + resource + " <globalheaders> node id=" + id + ",globalHeaders=" +
                globalHeaders + ".");

        GlobalHeadersResolver globalHeadersResolver = new GlobalHeadersResolver(builderAssistant, id, globalHeaders);
        globalHeadersResolver.resolve();

    }

    /**
     * 根据get|post|options|delete|head|put并构建statement
     * @param list
     */
    private void buildStatementFromContext(List<XNode> list) {
        for (XNode context : list) {
            //构建所有语句,一个mapper下可以有很多get,语句比较复杂，核心都在这里面，所以调用XMLStatementBuilder
            XMLStatementBuilder statementParser = new XMLStatementBuilder(configuration, builderAssistant, context);
            try {
                statementParser.parseStatementNode();
            } catch (IncompleteElementException e) {
                e.printStackTrace();
            }
        }
    }

    private void bindMapperForNamespace() {
        String namespace = builderAssistant.getCurrentNamespace();
        if (namespace != null) {
            Class<?> boundType = null;
            try {
                boundType = Resources.classForName(namespace);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (boundType != null) {
                if (!configuration.hasMapper(boundType)) {
                    configuration.addLoadedResource("namespace:" + namespace);
                    configuration.addMapper(boundType);
                }
            }
        }
    }
}