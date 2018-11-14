package com.crawler.artifact.binding;

import java.lang.reflect.Method;

import com.crawler.artifact.http.environment.RetryEntity;
import com.crawler.artifact.http.httpclient.HttpCrawler;
import com.crawler.artifact.http.httpclient.Response;
import com.crawler.artifact.session.Configuration;
import com.crawler.artifact.type.*;
import com.crawler.artifact.aspect.SessionSwitcher;
import com.crawler.artifact.http.environment.LoginCrawlerCookie;
import com.crawler.artifact.http.environment.ProxyManager;
import com.crawler.artifact.http.httpclient.Status;
import com.crawler.artifact.annocation.RequestMethod;
import com.crawler.artifact.logging.LogFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;

import java.util.*;

/**
 * Created by liuzhixiong on 2018/9/17.
 * LoginCrawler请求入口类
 */

public class HttpClientMapperMethod extends MapperMethod{

    HttpCrawler hc; //抓取器
    HttpMethodType httpMethodType = HttpMethodType.GET; //请求类型

    /**
     * 基本信息初始化
     * @param sessionID
     * @param mapperInterface
     * @param method
     * @param config
     */
    public HttpClientMapperMethod(String sessionID, Class<?> mapperInterface, Method method, Configuration config) {
        super(sessionID, mapperInterface, method, config);
        this.hc = getClient();
    }

    /**
     * 执行Http请求
     * @return
     */
    @Override
    public Object execute() {

        System.out.println("url=" + url);

        Response response = null;

        if(httpMethodType == HttpMethodType.SCHEDULE) {
            ScheduleExecutor executor = configuration.getSchedule(mappedStatementId);
            AuthCodeCacheBean authCodeCacheBean = executor.pop();
            response = new Response();
            response.setStatus(Status.SC_OK);
            response.setBufferedImage(authCodeCacheBean.getImage());
            response.setParameter(authCodeCacheBean.getParameter());
            response.setProxy(authCodeCacheBean.getProxy());
            this.hc = authCodeCacheBean.getHc();
        } else {
            if (hc == null) {
                response = new Response();
                response.setStatus(Status.SC_SESSION_TIMEOUT);
                return response;
            }

            int times = 1;
            if(retryEntity != null){
                times = retryEntity.getTimes();
            }

            for(int i=0; i<times; i++){
                LogFactory.logInfo("HttpClientMapperMethod execute()--sessionID=" + sessionID + ",mappedStatementId=" + mappedStatementId
                        + ",url=" + url + ",charset=" + charset + ",httpMethodType=" + httpMethodType + ",headers=" + headers +
                        ",params=" + params + ",proxy=" + (hc.getProxy() == null ? null : hc.getProxy().toHostString()) + ",retryEntity=" + retryEntity);
                response = HttpClientMethodType.match(httpMethodType).getHttpMethodStrategy().request(hc, url, params, headers, charset);
                if(checkPass(response)){
                    break;
                }
            }

            resumeDefaultProperties();

        }

        refreshCache();
        return response;
    }

    /**
     * 初始化请求方法
     */
    @Override
    public void initMethodType () {
        if(mappedStatement.getHttpMethodType() != null){
            this.httpMethodType = mappedStatement.getHttpMethodType();
        }
        RequestMethod requestMethod = method.getAnnotation(RequestMethod.class);
        HttpMethodType httpMethodType = requestMethod != null ? requestMethod.value() : null;
        if (httpMethodType != null) {
            this.httpMethodType = httpMethodType;
        }
    }

    /**
     * 初始化Cookie数据,Cookie数据通过接口传入
     * @param typeAndValue
     */
    @Override
    public void initCookies (Map<ParamaType, Object> typeAndValue) {
        if(typeAndValue.containsKey(ParamaType.COOKIES)){
            LoginCrawlerCookie loginCrawlerCookie = (LoginCrawlerCookie)typeAndValue.get(ParamaType.COOKIES);
            List<BasicClientCookie> cookies = loginCrawlerCookie.getCookies();
            for (BasicClientCookie cookie : cookies) {
                hc.addCookies(cookie);
            }
        }
    }

    /**
     * 初始化代理数据
     * 代理类型配置在mapper.xml的全局变量中
     */
    @Override
    public void initProxy (Map<ParamaType, Object> typeAndValue) {
        if(typeAndValue.containsKey(ParamaType.PROXY)){
            Object proxy = typeAndValue.get(ParamaType.PROXY);
            HttpHost host = ProxyManager.getHttpHost(proxy);
            hc.setProxy(host);
        }
    }

    /**
     * 检查单次请求是否通过,以决策是否重试
     * @param response
     * @return
     */
    public boolean checkPass(Response response){

        if (response == null) {
            return false;
        }

        List<RetryEntity.Mean> means = retryEntity.getMeans();
        for (RetryEntity.Mean mean:means) {
            String type = mean.getType().toUpperCase();
            String value = mean.getValue();
            if(RetryType.CODE.name().equals(type)){
                int stateCode = Integer.valueOf(value);
                if(response.getStatus().getStatecode() != stateCode){
                    return false;
                }
            }
            if(RetryType.CONTENT.name().equals(type)){
                if(!StringUtils.isNoneBlank(response.getContent()) || !response.getContent().contains(value)){
                    return false;
                }
            }
            if(RetryType.COOKIE.name().equals(type)){
                List<Cookie> cookies = response.getCookie();
                boolean flag = false;
                for (Cookie cookie:cookies) {
                    if(cookie.getName().equalsIgnoreCase(value)){
                        flag = true;
                        break;
                    }
                }
                return flag;
            }
            if(RetryType.LOCAITON.name().equals(type)){
                String location = response.getLocation();
                if(!StringUtils.isNoneBlank(location) || !location.startsWith(value)){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 获取抓取器,若存在,则从缓存获取;若不存在,则按照XML参数初始化
     * @return
     */
    @Override
    public HttpCrawler getClient() {
        HttpCrawler hc = null;
        if(start || sessionID == null){
            hc = initLoginCrawler();
        } else {
            Map<String, HttpCrawler> map = configuration.getMapperCache(mapperInterface.getName());
            if(map.containsKey(sessionID)){
                hc = map.get(sessionID);
            }
        }
        initProperties(hc);
        return hc;
    }

    /**
     * 初始化连接配置信息
     * @param hc
     */
    private void initProperties(HttpCrawler hc) {
        if(hc != null){
            Properties properties = mappedStatement.getVariables();
            initProperties(hc, properties);
        }
    }

    /**
     * 初始化连接配置信息
     * @param hc
     */
    private void initProperties(HttpCrawler hc, Properties properties) {
        if (properties != null) {
            Enumeration<?> enu = properties.propertyNames();
            while (enu.hasMoreElements()) {
                String key = (String)enu.nextElement();
                String val = (String)properties.get(key);
                initSingleProperty(hc, key, val);
            }
        }
    }

    /**
     * 初始化单个属性值
     * @param hc
     * @param key
     * @param value
     */
    private void initSingleProperty(HttpCrawler hc, String key, String value) {

        ConnectionType connectionType = ConnectionType.match(key.toUpperCase());
        if(connectionType == null){
            return;
        }

        if(connectionType == ConnectionType.TIMEOUT){
            hc.setTimeout(Integer.valueOf(value));
        } else if (connectionType == ConnectionType.CONNECTIONTIMEOUT){
            hc.setConnectionTimeout(Integer.valueOf(value));
        } else if(connectionType == ConnectionType.SOCKETTIMEOUT){
            hc.setSocketTimeout(Integer.valueOf(value));
        } else if (connectionType == ConnectionType.MAXTOTALCONNECTIONS){
            hc.setMaxTotalConnections(Integer.valueOf(value));
        } else if (connectionType == ConnectionType.MAXCONNECTIONPERHOST){
            hc.setMaxConnectionPerHost(Integer.valueOf(value));
        } else if (connectionType == ConnectionType.MAXTRYTIMESTOFETCH){
            hc.setMaxTryTimesToFetch(Integer.valueOf(value));
        } else if (connectionType == ConnectionType.ISFOLLOWREDIRECTS){
            hc.setIsFollowRedirects(Boolean.valueOf(value));
        }
    }

    /**
     * 恢复全局连接配置
     */
    private void resumeDefaultProperties() {
        boolean resume = mappedStatement.isResume();
        if (resume) {
            Properties properties = configuration.getVariables();
            initProperties(hc, properties);
        }
    }

    /**
     * 初始化抓取器
     * @return
     */
    private HttpCrawler initLoginCrawler() {
        return new HttpCrawler();
    }

    /**
     * 抓取器保存到缓存
     */
    private void refreshCache(){
        Map<String, HttpCrawler> map = configuration.getMapperCache(mapperInterface.getName());
        if(sessionID != null && map.containsKey(sessionID)){
            HttpCrawler cachedHc = map.get(sessionID);
            cachedHc.close();
            map.put(sessionID, hc);
        } else if(sessionID != null && !map.containsKey(sessionID)) {
            map.put(sessionID, hc);
        }
        SessionSwitcher.setSessionId(sessionID);
    }
}
