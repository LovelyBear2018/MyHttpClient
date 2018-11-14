package com.crawler.artifact.service;

/**
 * Created by liuzhixiong on 2018/10/29.
 */
public class InfoBean {
    String sessionId;


    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return "InfoBean{" +
                "sessionId='" + sessionId + '\'' +
                '}';
    }
}
