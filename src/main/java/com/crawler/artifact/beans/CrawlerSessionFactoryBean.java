package com.crawler.artifact.beans;

import java.io.IOException;
import java.util.Properties;

import com.crawler.artifact.exceptions.NestedIOException;
import com.crawler.artifact.session.Configuration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.FactoryBean;
import static org.springframework.util.Assert.notNull;
import static org.springframework.util.Assert.state;

import com.crawler.artifact.session.CrawlerSessionFactoryBuilder;
import com.crawler.artifact.session.CrawlerSessionFactory;
import com.crawler.artifact.builder.xml.XMLConfigBuilder;

/**
 * Created by liuzhixiong on 2018/9/25.
 */
public class CrawlerSessionFactoryBean implements FactoryBean<CrawlerSessionFactory>, InitializingBean{

    private Resource configLocation;
    private Configuration configuration;
    private CrawlerSessionFactoryBuilder crawlerSessionFactoryBuilder = new CrawlerSessionFactoryBuilder();
    private CrawlerSessionFactory crawlerSessionFactory;
    private Properties configurationProperties;

    @Override
    public void afterPropertiesSet() throws Exception {

        notNull(crawlerSessionFactoryBuilder, "Property 'crawlerSessionFactoryBuilder' is required");
        state((configuration == null && configLocation == null) || !(configuration != null && configLocation != null),
                "Property 'configuration' and 'configLocation' can not specified with together");

        this.crawlerSessionFactory = buildCrawlerSessionFactory();
    }

    @Override
    public CrawlerSessionFactory getObject() throws Exception {
        if (this.crawlerSessionFactory == null) {
            afterPropertiesSet();
        }

        return this.crawlerSessionFactory;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public Class<? extends CrawlerSessionFactory> getObjectType() {
        return this.crawlerSessionFactory == null ? CrawlerSessionFactory.class : this.crawlerSessionFactory.getClass();
    }

    protected CrawlerSessionFactory buildCrawlerSessionFactory() throws IOException{

        Configuration configuration;

        XMLConfigBuilder xmlConfigBuilder = null;

        if (this.configLocation != null) {
            xmlConfigBuilder = new XMLConfigBuilder(this.configLocation.getInputStream(), this.configurationProperties);
            configuration = xmlConfigBuilder.getConfiguration();
        } else {
            configuration = new Configuration();
            configuration.setVariables(this.configurationProperties);
        }

        if (xmlConfigBuilder != null) {
            try {
                xmlConfigBuilder.parse();
            } catch (Exception ex) {
                throw new NestedIOException("Failed to parse config resource: " + this.configLocation, ex);
            }
        }

        return this.crawlerSessionFactoryBuilder.build(configuration);
    }

    public void setConfigLocation(Resource configLocation) {
        this.configLocation = configLocation;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public void setConfigurationProperties(Properties configurationProperties) {
        this.configurationProperties = configurationProperties;
    }

    public Resource getConfigLocation() {
        return configLocation;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public Properties getConfigurationProperties() {
        return configurationProperties;
    }

}
