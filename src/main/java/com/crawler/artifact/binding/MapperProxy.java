package com.crawler.artifact.binding;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

import com.crawler.artifact.logging.LogFactory;
import com.crawler.artifact.reflection.ExceptionUtil;
import com.crawler.artifact.session.Configuration;
import com.crawler.artifact.session.CrawlerSession;
import com.crawler.artifact.aspect.SessionSwitcher;

/**
 * Created by liuzhixiong on 2018/9/17.
 * Mapper代理类,被代理类是mapper接口类
 */

public class MapperProxy<T> implements InvocationHandler, Serializable {

    private static final long serialVersionUID = -6424540398559729838L;
    private final CrawlerSession crawlerSession; //抓取会话
    private final Class<T> mapperInterface; //mapper类字节码

    /**
     * 初始化Mapper代理
     * @param crawlerSession
     * @param mapperInterface
     */
    public MapperProxy(CrawlerSession crawlerSession, Class<T> mapperInterface) {
        this.crawlerSession = crawlerSession;
        this.mapperInterface = mapperInterface;
    }

    /**
     * 代理以后，所有Mapper的方法调用时，都会调用这个invoke方法
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        String sessionID = SessionSwitcher.getSessionId();

        LogFactory.logInfo("invoke() sessionID=" + sessionID + ",mapperInterface=" + mapperInterface.getName() + "，method=" +
                method.getName() + ",args=" + Arrays.toString(args));

        /**
         * 不对Object类相关方法处理,例如打印mapper.toString(),若不处理,则无法找到MapperStatement
         */
        if (Object.class.equals(method.getDeclaringClass())) {
            try {
                return method.invoke(this, args);
            } catch (Throwable t) {
                throw ExceptionUtil.unwrapThrowable(t);
            }
        }

        Configuration configuration = crawlerSession.getConfiguration();

        MapperMethod mapperMethod = MapperMethod.newMapperMethod(sessionID, mapperInterface, method, configuration);
        Object result = mapperMethod.execute(args);

        LogFactory.logInfo("invoke() sessionID=" + sessionID + ",result=" + result);

        return result;
    }

}
