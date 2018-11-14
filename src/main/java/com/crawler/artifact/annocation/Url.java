package com.crawler.artifact.annocation;

import java.lang.annotation.*;

/**
 * Created by liuzhixiong on 2018/9/18.
 * 描述:URL相关注解
 * 字段:value 默认为空
 * 适用:mapper接口相关方法
 */

@Target({ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Url {
    String value() default "";
}
