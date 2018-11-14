package com.crawler.artifact.type;

import com.crawler.artifact.http.htmlunit.*;
import com.crawler.artifact.http.httpclient.IHttpMethodStrategy;

/**
 * Created by liuzhixiong on 2018/10/16.
 */
public enum HtmlunitMethodType {

    GET("GET", new HutGetMethodStrategy()),
    POST("POST", new HutPostMethodStrategy()),
    OPTIONS("OPTIONS", new HutOptionMethodStrategy()),
    DELETE("DELETE", new HutDeleteMethodStrategy()),
    HEAD("HEAD", new HutHeadMethodStrategy()),
    PUT("PUT", new HutPutMethodStrategy());

    HtmlunitMethodType(String methodName, IHttpMethodStrategy httpMethodStrategy){
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

    public static HtmlunitMethodType match(HttpMethodType httpMethodType){
        for(HtmlunitMethodType htmlunitMethodType:HtmlunitMethodType.values()){
            if(htmlunitMethodType.name().equals(httpMethodType.name())){
                return htmlunitMethodType;
            }
        }
        return null;
    }

}
