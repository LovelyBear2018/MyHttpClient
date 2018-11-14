package com.crawler.artifact.http.httpclient;

/**
 * Created by liuzhixiong on 2018/11/14.
 */
public enum LcStatus {
    INIT,PENDING,RUNNING,CLOSE; //初始状态、挂起状态、运行状态、关闭

    public static LcStatus match(String lcStatusStr){

        for(LcStatus lcStatus:LcStatus.values()){
            if(lcStatus.name().equals(lcStatusStr)){
                return lcStatus;
            }
        }

        return null;
    }
}
