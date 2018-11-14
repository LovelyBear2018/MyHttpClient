package com.crawler.artifact.type;

import com.crawler.artifact.http.httpclient.*;

/**
 * Created by liuzhixiong on 2018/10/16.
 */
public enum HttpClientMethodType {

    GET("GET", new HcGetMethodStrategy()),
    POST("POST", new HcPostMethodStrategy()),
    OPTIONS("OPTIONS", new HcOptionMethodStrategy()),
    DELETE("DELETE", new HcDeleteMethodStrategy()),
    HEAD("HEAD", new HcHeadMethodStrategy()),
    PUT("PUT", new HcPutMethodStrategy());

    HttpClientMethodType(String methodName, IHttpMethodStrategy httpMethodStrategy){
        this.methodName = methodName;
        this.httpMethodStrategy = httpMethodStrategy;
    }

    private String methodName;
    private IHttpMethodStrategy httpMethodStrategy;

    public String getMethodName() {
        return methodName;
    }

    public IHttpMethodStrategy getHttpMethodStrategy() {
        return httpMethodStrategy;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setHttpMethodStrategy(IHttpMethodStrategy httpMethodStrategy) {
        this.httpMethodStrategy = httpMethodStrategy;
    }

    public static HttpClientMethodType match(HttpMethodType httpMethodType){
        for(HttpClientMethodType httpClientMethodType:HttpClientMethodType.values()){
            if(httpClientMethodType.name().equals(httpMethodType.name())){
                return httpClientMethodType;
            }
        }
        return null;
    }
}
