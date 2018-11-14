package com.crawler.artifact.annocation;

import java.lang.annotation.*;

/**
 * Created by liuzhixiong on 2018/10/24.
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Session {
}
