package com.crawler.artifact.logging;

import com.crawler.artifact.type.LogLevel;

/**
 * Created by liuzhixiong on 2018/9/29.
 */

public class LogFactory{

    private static Log logger;
    private static LogLevel level;

    /**
     * 初始化日志组件
     * @param type
     * @param levelStr
     */
    public static void createLog(String type, String levelStr){
        Log log = null;
        switch(type){
            case "log4j":
                log = new Log4JImpl();
        }

        logger = log;
        level = LogLevel.match(levelStr);
    }

    public static void logInfo(String s) {
        if(level == LogLevel.INFO){
            info(s);
        } else if(level == LogLevel.DEBUG) {
            debug(s);
        } else if(level == LogLevel.WARN) {
            warn(s);
        } else if(level == LogLevel.ERROR) {
            error(s);
        }
    }

    /**
     * info级别
     * @param s
     */
    public static void info(String s){
        if (logger != null) {
            logger.info(s);
        }
    }

    /**
     * debug
     * @param s
     */
    public static void debug(String s) {
        if (logger != null) {
            logger.debug(s);
        }
    }

    /**
     * warn
     * @param s
     */
    public static void warn(String s) {
        if (logger != null) {
            logger.warn(s);
        }
    }

    /**
     * error
     * @param s
     */
    public static void error(String s) {
        if (logger != null) {
            logger.error(s);
        }
    }
}
