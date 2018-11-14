package com.crawler.artifact.exceptions;

/**
 * Created by liuzhixiong on 2018/9/17.
 * 异常工厂类
 */
public class ExceptionFactory {

    /*
     * 私有化构造方法防止实例化
     */
    private ExceptionFactory() {
    }

    //把普通异常包装成CrawlerClient自己的PersistenceException
    public static RuntimeException wrapException(String message, Exception e) {
        //查找错误上下文，得到错误原因，传给PersistenceException
        //每个线程都会有一个ErrorContext，所以可以得到，  .message(message).cause是典型的构建器模式
        return new PersistenceException(ErrorContext.instance().message(message).cause(e).toString(), e);
    }
}
