package com.crawler.artifact.type;

/**
 * Created by liuzhixiong on 2018/9/20.
 * 编码格式类型:枚举
 */

public enum CharSetType {

    UTF8("utf-8"),GBK("gbk"),GB2312("gb2312"),ISO("iso-8859-1");

    String value;

    CharSetType(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
