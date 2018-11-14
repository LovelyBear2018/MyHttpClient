package com.crawler.artifact.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * Created by liuzhixiong on 2018/9/17.
 * 异常工具
 */
public class ExceptionUtil {

    private ExceptionUtil() {
        // Prevent Instantiation
    }

    public static Throwable unwrapThrowable(Throwable wrapped) {
        Throwable unwrapped = wrapped;
        while (true) {
            //处理2种异常，InvocationTargetException和UndeclaredThrowableException，将它们解包,从而得到真正的异常
            if (unwrapped instanceof InvocationTargetException) {
                unwrapped = ((InvocationTargetException) unwrapped).getTargetException();
            } else if (unwrapped instanceof UndeclaredThrowableException) {
                unwrapped = ((UndeclaredThrowableException) unwrapped).getUndeclaredThrowable();
            } else {
                return unwrapped;
            }
        }
    }

}
