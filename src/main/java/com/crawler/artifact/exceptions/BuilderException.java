package com.crawler.artifact.exceptions;

/**
 * Created by liuzhixiong on 2018/9/17.
 * 构建异常,继承PersistenceException
 */
public class BuilderException extends PersistenceException {
    private static final long serialVersionUID = -3885164021020443281L;

    public BuilderException() {
        super();
    }

    public BuilderException(String message) {
        super(message);
    }

    public BuilderException(String message, Throwable cause) {
        super(message, cause);
    }

    public BuilderException(Throwable cause) {
        super(cause);
    }
}
