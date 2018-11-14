package com.crawler.artifact.annocation;

import java.lang.annotation.*;

/**
 * Created by liuzhixiong on 2018/9/18.
 * 描述:应用开发人员可以传入URL,HEADERS,PARAMS等参数,框架底层解析参数并结合XML配置生成实际参数
 * 字段:value 枚举类型,防止应用开发人员使用出错
 * 适用:mapper接口相关方法的形参列表
 */

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Headers {
}
