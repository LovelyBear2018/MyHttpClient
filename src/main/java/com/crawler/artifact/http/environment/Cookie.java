package com.crawler.artifact.http.environment;

import java.util.Date;

/**
 * Created by liuzhixiong on 2018/11/14.
 */
public class Cookie {
    String name;
    String value;
    String domain;
    String path;
    Date expires;
    String version;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "UmeCookie [name=" + name + ", value=" + value + ", domain=" + domain + ", path=" + path + ", expires="
                + expires + ", version=" + version + "]";
    }

}
