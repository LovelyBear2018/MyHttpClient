package com.crawler.artifact.aspect;

/**
 * Created by liuzhixiong on 2018/9/20.
 * 保存SessionID上下文信息,通过ThreadLocal实现
 */
public class SessionSwitcher {

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public static void setSessionId(String sessionId){
        contextHolder.set(sessionId);
    }

    public static String getSessionId(){
        return contextHolder.get();
    }

    public static void clearSessionId(){
        contextHolder.remove();
    }

}
