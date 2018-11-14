package com.crawler.artifact.type;

/**
 * Created by liuzhixiong on 2018/9/29.
 * 四大日志级别
 */

public enum LogLevel {

    INFO,DEBUG,WARN,ERROR;

    public static LogLevel match(String fcTypeStr){
        for(LogLevel logLevel:LogLevel.values()){
            if(logLevel.name().equalsIgnoreCase(fcTypeStr)){
                return logLevel;
            }
        }
        return null;
    }
}
