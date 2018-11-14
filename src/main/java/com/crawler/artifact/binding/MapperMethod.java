package com.crawler.artifact.binding;

import com.crawler.artifact.reflection.ReflectionUtil;
import com.crawler.artifact.util.SessionUtil;
import com.crawler.artifact.annocation.Charset;
import com.crawler.artifact.annocation.Crawler;
import com.crawler.artifact.annocation.Start;
import com.crawler.artifact.annocation.Url;
import com.crawler.artifact.hander.ParameterHandler;
import com.crawler.artifact.http.environment.RetryEntity;
import com.crawler.artifact.mapping.MappedStatement;
import com.crawler.artifact.session.Configuration;
import com.crawler.artifact.type.CharSetType;
import com.crawler.artifact.type.CrawlerType;
import com.crawler.artifact.type.ParamaType;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuzhixiong on 2018/10/15.
 */
public abstract class MapperMethod {

    String sessionID;
    Class<?> mapperInterface; //mapper类字节码
    Method method; //mapper方法
    String  mappedStatementId; //每一个mappedStatementId对应一个MappedStatement,mappedStatementId格式:包名+类名+方法名
    MappedStatement mappedStatement; //每一个MappedStatement对应一个请求的XML配置信息
    String url; //请求url
    Map<String, String> headers = new HashMap<String, String>(); //请求头
    String charset = CharSetType.UTF8.getValue(); //请求编码
    Object params; //请求参数
    RetryEntity retryEntity; //重试参数
    HttpHost proxy; //代理
    boolean start = false; //是否为第一个请求方法,由@Start注解得到
    Configuration configuration;

    public MapperMethod(String sessionID, Class<?> mapperInterface, Method method, Configuration configuration){
        if (StringUtils.isBlank(sessionID)) sessionID = SessionUtil.createSessionId();
        this.sessionID = sessionID;
        this.configuration = configuration;
        this.mapperInterface = mapperInterface;
        this.mappedStatementId = getMappedStatementId(mapperInterface, method);
        this.mappedStatement = configuration.getMappedStatement(mappedStatementId);
        this.method = method;
        if(method.getAnnotation(Start.class) != null){
            start = true;
        }
        if(mappedStatement != null){
            this.retryEntity = mappedStatement.getRetryEntity();
        }
    }

    /**
     * 实例化MapperMethod
     * @param sessionID
     * @param mapperInterface
     * @param method
     * @param configuration
     * @return
     */
    public static MapperMethod newMapperMethod(String sessionID, Class<?> mapperInterface, Method method, Configuration configuration) {

        /*
         * 抓取器类型有两个地方可以设置
         * 1、mapper标签的 crawlertype 属性
         * 2、*Mapper接口的 CrawlerType注解
         * 注:< 2 优先级大于 1 优先级> <若1和2都未配置,则默认抓取器为httpclient>
         */

        String mappedStatementId = getMappedStatementId(mapperInterface, method);
        CrawlerType crawlerType = configuration.getGlobalCrawlerType(mappedStatementId);

        if(crawlerType == null){
            Class<?> mapperClass = method.getDeclaringClass();
            Crawler crawlerAnno = mapperClass.getAnnotation(Crawler.class);
            if(crawlerAnno == null){
                crawlerType = CrawlerType.HTTPCLIENT;
            } else {
                crawlerType = crawlerAnno.value();
            }
        }

        if(crawlerType == CrawlerType.HTTPCLIENT){
            return new HttpClientMapperMethod(sessionID, mapperInterface, method, configuration);
        } else if (crawlerType == CrawlerType.HTMLUNIT) {
            return new HtmlUnitMapperMethod(sessionID, mapperInterface, method, configuration);
        } else if (crawlerType == CrawlerType.SELENIUM) {
            return new SeleniumMapperMethod(sessionID, mapperInterface, method, configuration);
        }

        return null;
    }

    /**
     * 拼接获取mappedStatementId:包名+类名+方法名
     * @param mapperInterface
     * @param method
     * @return
     */
    private static String getMappedStatementId(Class<?> mapperInterface, Method method){
        return mapperInterface.getName() + "." + method.getName();
    }

    public String catKey(){
        return mapperInterface.getSimpleName() + "." + method.getName();
    }

    /**
     * 执行Http请求
     * @return
     */
    protected Object execute(Object[] args) {
        init(args);
        return execute();
    }

    protected abstract Object execute();

    protected abstract Object getClient();

    /**
     * 初始化各类参数
     * @param args
     */
    private void init(Object[] args){

        try {
            Map<ParamaType, Object> typeAndValue = ReflectionUtil.getMethodParameterNamesByAnnotation(method, args);
            initUrl(typeAndValue);
            initMethodType();
            initCharSet();
            initHeaders(typeAndValue);
            initParams(typeAndValue);
            initCookies(typeAndValue);
            initProxy(typeAndValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化URL信息
     * 接口>注解>XML
     * @param typeAndValue
     */
    private void initUrl (Map<ParamaType, Object> typeAndValue) {
        this.url = mappedStatement.getUrl();
        Url urlAnn = method.getAnnotation(Url.class);
        String url = urlAnn != null ? urlAnn.value() : null;
        if (url != null) {
            this.url = url;
        }
        if(typeAndValue.containsKey(ParamaType.URL) && StringUtils.isNoneBlank((String)typeAndValue.get(ParamaType.URL))){
            this.url = (String)typeAndValue.get(ParamaType.URL);
        }
    }

    /**
     * 初始化请求类型
     * 注解>XML
     */
    public abstract void initMethodType ();

    /**
     * 初始化编码类型
     * 注解>XML
     */
    private void initCharSet () {
        if (mappedStatement.getCharset() != null) {
            this.charset = mappedStatement.getCharset();
        }
        Charset charSet = method.getAnnotation(Charset.class);
        CharSetType charSetType = charSet != null ? charSet.value() : null;
        if (charSetType != null) {
            this.charset = charSetType.getValue();
        }
    }

    /**
     * 初始化请求头
     * 接口>=XML
     * 动态参数初始化:
     * 1、若参数类型为Map,则与XML配置请求头合并,若请求头相同,则覆盖XML配置;
     * 2、若参数类型为String[],则按序对XML里面的动态参数赋值,动态参数格式为{0}
     * @param typeAndValue
     */
    private void initHeaders (Map<ParamaType, Object> typeAndValue) {
        if (mappedStatement.getHeaders() != null) {
            this.headers = mappedStatement.getHeaders();
        }
        if(typeAndValue.containsKey(ParamaType.HEADERS)){
            Object headers = typeAndValue.get(ParamaType.HEADERS);
            if (headers != null) {
                if(headers instanceof Map){
                    this.headers.putAll((Map<String, String>)headers);
                } else if (headers instanceof String[]){
                    ParameterHandler.setParameters(this.headers, (String[])headers);
                } else {
                    throw new IllegalArgumentException("类:" + mapperInterface.getSimpleName() + ",方法:" + method.getName() + ",参数:headers,类型错误,正确类型为(Map或String[])");
                }
            }
        }
    }

    /**
     * 初始化请求数据
     * 接口>=XML
     * 动态参数初始化:
     * 1、若参数类型为Map,则与XML配置请求数据合并,若请求头相同,则覆盖XML配置;
     * 2、若参数类型为String[],则按序对XML里面的动态参数赋值,动态参数格式为{0};
     * 3、若参数类型为String,则不考虑XML数据,使用接口数据作为请求数据
     * @param typeAndValue
     */
    private void initParams (Map<ParamaType, Object> typeAndValue) {
        if(mappedStatement.getParams() != null){
            this.params = mappedStatement.getParams();
        }
        if(typeAndValue.containsKey(ParamaType.PARAMS)){
            Object params = typeAndValue.get(ParamaType.PARAMS);
            if (params != null) {
                if(params instanceof Map){
                    if (this.params == null) {
                        this.params = new HashMap<String, String>();
                    }
                    ((Map<String, String>)this.params).putAll((Map<String, String>)params);
                } else if (params instanceof String[]){
                    ParameterHandler.setParameters((Map<String, String>)this.params, (String[])params);
                } else if (params instanceof String){
                    this.params = params;
                } else {
                    throw new IllegalArgumentException("类:" + mapperInterface.getSimpleName() + ",方法:" + method.getName() + ",参数:params,类型错误,正确类型为(Map或String[])");
                }
            }
        }
    }

    /**
     * 初始化Cookie数据,Cookie数据通过接口传入
     * @param typeAndValue
     */
    public abstract void initCookies (Map<ParamaType, Object> typeAndValue);

    /**
     * 初始化代理数据
     * 代理类型配置在mapper.xml的全局变量中
     */
    public abstract void initProxy (Map<ParamaType, Object> typeAndValue);

}
