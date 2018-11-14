package com.crawler.artifact.logging;

/**
 * Created by liuzhixiong on 2018/9/28.
 * 日志接口
 */

public interface Log {


    void info(String s);

    void debug(String s);

    void warn(String s);

    void error(String s);

}
