package com.crawler.artifact.binding;

import com.crawler.artifact.util.CacheList;
import com.crawler.artifact.http.httpclient.AuthCodeProcessor;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by liuzhixiong on 2018/10/22.
 */
public class ScheduleExecutor {

    private AuthCodeProcessor authCodeProcessor;
    private Method buildImageCodeMethod;
    int threadnum = 5;
    int timeout = 10 * 60;
    ExecutorService executorService = null;
    private CacheList cacheList;


    public ScheduleExecutor(AuthCodeProcessor authCodeProcessor, String threadnumStr, String timeoutStr) {
        this.authCodeProcessor = authCodeProcessor;

        try{
            buildImageCodeMethod = authCodeProcessor.getClass().getMethod("buildAuthCodeCacheBean");
        }catch (NoSuchMethodException e){
            e.printStackTrace();
        }

        if(StringUtils.isNotBlank(threadnumStr)){
            this.threadnum = Integer.valueOf(threadnumStr);;
        }
        executorService = Executors.newFixedThreadPool(threadnum);

        if(StringUtils.isNotBlank(timeoutStr)){
            this.timeout = Integer.valueOf(timeoutStr);
        }

        cacheList = new CacheList(timeout);
    }

    public void start(){
        for (int i = 0; i < threadnum; i++) {
            executorService.submit(new Runnable() {
                public void run() {
                    cache();
                }
            });
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void cache(){
        try{
            AuthCodeCacheBean authCodeCacheBean = (AuthCodeCacheBean)buildImageCodeMethod.invoke(authCodeProcessor);
            if(authCodeCacheBean != null){
                this.cacheList.add(authCodeCacheBean);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public AuthCodeCacheBean pop(){
        synchronized (this.cacheList) {
            while(this.cacheList.size() > 0){
                AuthCodeCacheBean authCodeCacheBean = this.cacheList.remove(0);
                if(authCodeCacheBean.getState() == 1){
                    return authCodeCacheBean;
                }
            }
        }
        return null;
    }

    public AuthCodeProcessor getAuthCodeProcessor() {
        return authCodeProcessor;
    }

    public Method getBuildImageCodeMethod() {
        return buildImageCodeMethod;
    }

    public int getThreadnum() {
        return threadnum;
    }

    public int getTimeout() {
        return timeout;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void setAuthCodeProcessor(AuthCodeProcessor authCodeProcessor) {
        this.authCodeProcessor = authCodeProcessor;
    }

    public void setBuildImageCodeMethod(Method buildImageCodeMethod) {
        this.buildImageCodeMethod = buildImageCodeMethod;
    }

    public void setThreadnum(int threadnum) {
        this.threadnum = threadnum;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public CacheList getCheList() {
        return cacheList;
    }

    public void setCheList(CacheList cheList) {
        this.cacheList = cheList;
    }
}
