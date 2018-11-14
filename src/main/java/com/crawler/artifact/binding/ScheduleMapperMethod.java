package com.crawler.artifact.binding;

import com.crawler.artifact.http.httpclient.HttpCrawler;
import com.crawler.artifact.session.Configuration;
import com.crawler.artifact.type.ParamaType;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by liuzhixiong on 2018/10/22.
 */
public class ScheduleMapperMethod extends MapperMethod{

    /**
     * 基本信息初始化
     * @param sessionID
     * @param mapperInterface
     * @param method
     * @param config
     */
    public ScheduleMapperMethod(String sessionID, Class<?> mapperInterface, Method method, Configuration config) {
        super(sessionID, mapperInterface, method, config);
    }

    /**
     * 执行Http请求
     * @return
     */
    @Override
    public Object execute() {
        return null;
    }

    /**
     * 初始化代理数据
     * 代理类型配置在mapper.xml的全局变量中
     */
    @Override
    public void initProxy (Map<ParamaType, Object> typeAndValue) {

    }

    /**
     * 获取抓取器,若存在,则从缓存获取;若不存在,则按照XML参数初始化
     * @return
     */
    @Override
    public HttpCrawler getClient() {
        return null;
    }

    /**
     * 初始化Cookie数据,Cookie数据通过接口传入
     * @param typeAndValue
     */
    @Override
    public void initCookies (Map<ParamaType, Object> typeAndValue) {

    }

    /**
     * 初始化请求方法类型
     */
    @Override
    public void initMethodType () {

    }

}
