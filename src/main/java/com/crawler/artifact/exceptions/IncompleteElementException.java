package com.crawler.artifact.exceptions;

/**
 * Created by liuzhixiong on 2018/9/17.
 * 元素不全异常,比如XMLIncludeTransformer里使用
 */
public class IncompleteElementException extends BuilderException {
    private static final long serialVersionUID = -3697292286890900315L;

    public IncompleteElementException() {
        super();
    }

    public IncompleteElementException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncompleteElementException(String message) {
        super(message);
    }

    public IncompleteElementException(Throwable cause) {
        super(cause);
    }

}
