package com.crawler.artifact.logging;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by liuzhixiong on 2018/9/29.
 * Log4J日志组件实现
 */
public class Log4JImpl implements com.crawler.artifact.logging.Log {

    Log logger = LogFactory.getLog("crawlerclient");

    public void info(String s) {
        logger.info(getHeader() + s);
    }

    public void debug(String s) {
        logger.debug(getHeader() + s);
    }

    public void warn(String s) {
        logger.warn(getHeader() + s);
    }

    public void error(String s) {
        logger.error(getHeader() + s);
    }

    private static String getHeader() {
        String threadInfo = Thread.currentThread().getName();
        String header="\"" + threadInfo + "\",";
        return header;
    }

    public static void main(String[] args) {
        Log logger = LogFactory.getLog("crawlerclient");
        logger.info("123");
    }

}
