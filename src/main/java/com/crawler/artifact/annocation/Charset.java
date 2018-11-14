package com.crawler.artifact.annocation;

import com.crawler.artifact.type.CharSetType;

import java.lang.annotation.*;

/**
 * Created by liuzhixiong on 2018/9/20.
 * 描述:编码类别相关注解
 * 字段:value 枚举类型,防止应用开发人员使用出错
 * 适用:mapper接口相关方法
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Charset {
    CharSetType value() default CharSetType.UTF8;
}
