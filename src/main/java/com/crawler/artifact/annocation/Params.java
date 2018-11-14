package com.crawler.artifact.annocation;

import java.lang.annotation.*;

/**
 * Created by liuzhixiong on 2018/10/26.
 */

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Params {
}
