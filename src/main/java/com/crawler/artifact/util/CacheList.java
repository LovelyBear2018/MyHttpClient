package com.crawler.artifact.util;

import com.crawler.artifact.binding.AuthCodeCacheBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by liuzhixiong on 2018/10/23.
 */

public class CacheList {

    long cacheTimeout;
    List<AuthCodeCacheBean> cacheList = new ArrayList<AuthCodeCacheBean>();

    public CacheList(long millisSeconds) {
        this.cacheTimeout = millisSeconds;
        new CacheList.ClearThread().start();
    }

    public boolean add(AuthCodeCacheBean e) {
        e.setDate(new Date());
        return cacheList.add(e);
    }

    public int size() {
        return cacheList.size();
    }

    public AuthCodeCacheBean remove(int index) {
        return cacheList.remove(index);
    }

    private class ClearThread extends Thread {
        ClearThread() {
            setName("clear cache thread");
        }

        public void run() {
            while (true) {
                try {
                    Date now = new Date();
                    for(AuthCodeCacheBean authCodeCacheBean:cacheList){
                        if((now.getTime() - authCodeCacheBean.getDate().getTime()) / 1000 >= cacheTimeout){
                            authCodeCacheBean.setState(0);
                        }
                    }
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
