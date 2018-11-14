package com.crawler.artifact.type;

/**
 * 连接参数类型
 * Created by liuzhixiong on 2018/10/19.
 */
public enum ConnectionType {
    TIMEOUT,
    CONNECTIONTIMEOUT,
    SOCKETTIMEOUT,
    MAXTOTALCONNECTIONS,
    MAXCONNECTIONPERHOST,
    MAXTRYTIMESTOFETCH,
    ISFOLLOWREDIRECTS;

    public static ConnectionType match(String connectionTypeStr){
        for(ConnectionType connectionType:ConnectionType.values()){
            if(connectionType.name().equals(connectionTypeStr)){
                return connectionType;
            }
        }
        return null;
    }
}
