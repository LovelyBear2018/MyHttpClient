package com.crawler.artifact.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by liuzhixiong on 2018/9/26.
 */

public class ServiceFactory {

    private static ApplicationContext ctx = null;

    public static ApplicationContext getContext() {
        if (ctx == null) {
            try {
                String tFile = "applicationContext-CrawlerClient-proxy.xml";
                ctx = new ClassPathXmlApplicationContext(
                        new String[] { "classpath:" + tFile });
            } catch (BeansException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return ctx;
    }

    public static Object getBean(String beanName){

        if(ctx == null){
            getContext();
        }

        return ctx.getBean(beanName);
    }
}
