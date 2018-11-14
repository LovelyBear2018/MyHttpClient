package com.crawler.artifact.annocation;

import java.lang.annotation.*;

/**
 * Created by liuzhixiong on 2018/10/19.
 * 描述:抓取开始的标志,用于 new LoginCrawler
 * 适用:mapper接口级别
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Start {
}
