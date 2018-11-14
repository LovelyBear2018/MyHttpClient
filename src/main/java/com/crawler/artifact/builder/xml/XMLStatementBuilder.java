package com.crawler.artifact.builder.xml;

import java.util.*;

import com.crawler.artifact.builder.MapperBuilderAssistant;
import com.crawler.artifact.http.environment.RetryEntity;
import com.crawler.artifact.node.Element;
import com.crawler.artifact.type.HttpMethodType;
import com.crawler.artifact.builder.BaseBuilder;
import com.crawler.artifact.exceptions.BuilderException;
import com.crawler.artifact.logging.LogFactory;
import com.crawler.artifact.node.Attribute;
import com.crawler.artifact.type.CharSetType;
import com.crawler.artifact.parsing.XNode;
import com.crawler.artifact.session.Configuration;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by liuzhixiong on 2018/9/17.
 * XML语句构建器，建造者模式,继承BaseBuilder
 */
public class XMLStatementBuilder extends BaseBuilder {

    private XNode context;

    public XMLStatementBuilder(Configuration configuration, MapperBuilderAssistant builderAssistant, XNode context) {
        super(configuration);
        this.builderAssistant = builderAssistant;
        this.context = context;
    }

    /**
     * eg
     * <get id="ffpLogin_Member">
     * <url value="http://ffp.airchina.com.cn/" />
     * <headers refid="caheaders" />
     * <retry>
     * <times value="3" />
     * <means>
     * <mean type="code" value="200" />
     * <mean type="content" value="NamePlace" />
     * </means>
     * <re-proxy />
     * </retry>
     * </get>
     */
    public void parseStatementNode() {

        String id = context.getStringAttribute(Attribute.id.name());

        //获取请求类型(schedule|get|post|options|delete|head|put)
        String nodeName = context.getNode().getNodeName();
        HttpMethodType httpMethodType = HttpMethodType.valueOf(nodeName.toUpperCase(Locale.ENGLISH));

        if (httpMethodType == HttpMethodType.SCHEDULE) {

            String scheduleId = applyCurrentNamespace(builderAssistant.getCurrentNamespace(), id);

            String location = context.getStringAttribute(Attribute.location.name());
            String threadnum = context.getStringAttribute(Attribute.threadnum.name());
            String timeout = context.getStringAttribute(Attribute.timeout.name());
            configuration.addSchedule(scheduleId, location, threadnum, timeout);
        }

        XNode urlNode = context.evalNode(Element.url.name());
        String url = null;
        if (urlNode != null) {
            url = urlNode.getStringAttribute(Attribute.value.name());
        }

        XNode charsetNode = context.evalNode(Element.charset.name());
        String charset;
        if (charsetNode != null) {
            charset = charsetNode.getStringBody();
        } else {
            String globalCharset = configuration.getGlobalCharset(builderAssistant.getCurrentNamespace());
            if (StringUtils.isNotBlank(globalCharset)) {
                charset = globalCharset;
            } else {
                charset = CharSetType.UTF8.getValue();
            }
        }

        XNode headersNode = context.evalNode(Element.headers.name());
        Map<String, String> headers = new HashMap<String, String>();
        fillMap(headers, headersNode);

        XNode paramsNode = context.evalNode(Element.params.name());
        Map<String, String> params = new HashMap<String, String>();
        fillMap(params, paramsNode);

        XNode retryNode = context.evalNode(Element.retry.name());
        RetryEntity retryEntity = new RetryEntity();
        parseRetryEntity(retryEntity, retryNode);

        XNode propertiesNode = context.evalNode(Element.properties.name());
        Properties properties = (Properties) configuration.getVariables().clone();
        boolean resume = false;
        if (propertiesNode != null) {
            properties.putAll(propertiesNode.getChildrenAsProperties());
            String resumeStr = propertiesNode.getStringAttribute(Attribute.resume.name());
            if (StringUtils.isNotBlank(resumeStr)) {
                resume = Boolean.valueOf(resumeStr);
            }
        }

        //调用助手类
        builderAssistant.addMappedStatement(id, url, httpMethodType, headers, params, retryEntity, charset, properties, resume);
    }

    /**
     * 为id加上namespace前缀，如ffpLogin-->org.a.b.ffpLogin
     * @param base
     * @return
     */
    public String applyCurrentNamespace(String currentNamespace, String base) {
        if (base == null) {
            return null;
        }
        if (base.startsWith(currentNamespace + ".")) {
            return base;
        }
        if (base.contains(".")) {
            throw new BuilderException("Dots are not allowed in element names, please remove it from " + base);
        }
        return currentNamespace + "." + base;
    }


    /**
     * 解析重试配置信息
     *
     * @param retryEntity
     * @param node
     */
    private void parseRetryEntity(RetryEntity retryEntity, XNode node) {
        LogFactory.logInfo("parsing " + this.builderAssistant.getResource() + " <retry> node.");
        if (node != null) {
            XNode timesNode = node.evalNode(Element.times.name()); //重试次数
            int times = Integer.valueOf(timesNode.getStringAttribute(Attribute.value.name()));
            retryEntity.setTimes(times);

            XNode meansNode = node.evalNode(Element.means.name()); //判断重试条件
            if (meansNode != null) {
                List<XNode> meanNodes = meansNode.evalNodes(Element.mean.name());
                for (XNode meanNode : meanNodes) {
                    String type = meanNode.getStringAttribute(Attribute.type.name());
                    String value = meanNode.getStringAttribute(Attribute.value.name());
                    retryEntity.addMean(type, value);
                }
            }

            LogFactory.logInfo("parsing " + this.builderAssistant.getResource() + " <retry> node retryEntity=" + retryEntity + ".");
        } else {
            LogFactory.logInfo(this.builderAssistant.getResource() + " <retry> node is null.");
        }
    }
}
