package com.crawler.artifact.type;

/**
 * Created by liuzhixiong on 2018/9/17.
 * Http六大请求方法,以及对应策略实现实例
 */

public enum HttpMethodType {

    SCHEDULE,
    GET,
    POST,
    OPTIONS,
    DELETE,
    HEAD,
    PUT;

    public static HttpMethodType match(String httpMethodTypeStr){
        for(HttpMethodType httpMethodType:HttpMethodType.values()){
            if(httpMethodType.name().equals(httpMethodTypeStr.toUpperCase())){
                return httpMethodType;
            }
        }
        return null;
    }

}
