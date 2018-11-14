package com.crawler.artifact.annocation;

import java.lang.annotation.*;

/**
 * Created by liuzhixiong on 2018/9/25.
 * 描述:代理相关注解,@Proxy可以结合AOP通过后置通知获取ResultCode以及Proxy,帮助开发人员实现代理更新功能
 * 字段: serviceType 应用服务类型,若为空则寻找XML代理全局配置
 *      fcType 航司类型
 *      strategy 代理策略
 *      resultCode 需要验证的ResultCode,若需要验证多个ResultCode则以逗号分隔
 * 适用:Service类相关方法 & 带sessionID参数 & sessionID参数为第一个参数
 */

@Target({ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Proxy {
    String serviceType() default "";
    String fcType() default "";
    String strategy() default "";
    String resultCode() default "1";
}
