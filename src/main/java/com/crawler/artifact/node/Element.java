package com.crawler.artifact.node;

/**
 * Created by liuzhixiong on 2018/10/25.
 */
public enum Element {
    configuration,
    settings,properties,mappers,
    log,cache,cat,
    pack,mapper,
    globalheaders,charset,proxy,
    schedule,get,post,options,delete,head,put,
    url,headers,params,retry,
    times,means,mean,re_proxy;

    public static Element match(String elementStr){
        for(Element element:Element.values()){
            if(element.name().equalsIgnoreCase(elementStr)){
                return element;
            }
        }
        return null;
    }

    public static String listRequestMethod(){
        String result = schedule.name() + "|" + get.name() + "|" + post.name() + "|" + options.name() + "|"
                + delete.name() + "|" + head.name() + "|" + put.name();
        return result;
    }

    public static String listSettingsElement(){
        String result = Element.log.name() + "|" + Element.cache.name() + "|" + Element.cat.name();
        return result;
    }



}
