package com.crawler.artifact.annocation;

import com.crawler.artifact.type.CrawlerType;

import java.lang.annotation.*;

/**
 * Created by liuzhixiong on 2018/10/15.
 * 描述:抓取器类别相关注解
 * 字段:value 枚举类型,防止应用开发人员使用出错
 * 适用:mapper接口级别
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Crawler {
    CrawlerType value() default CrawlerType.HTTPCLIENT;
}
