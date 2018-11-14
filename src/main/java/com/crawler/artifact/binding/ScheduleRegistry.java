package com.crawler.artifact.binding;

import com.crawler.artifact.io.Resources;
import com.crawler.artifact.http.httpclient.AuthCodeProcessor;
import com.crawler.artifact.session.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuzhixiong on 2018/10/22.
 */

public class ScheduleRegistry {

    private Configuration configuration;
    private static Map<String, ScheduleExecutor> knownSchedules = new HashMap<String, ScheduleExecutor>(); //已注册定时任务

    public ScheduleRegistry(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * 添加Schedule
     * @param id
     * @param location
     */
    public <T> void addSchedule(String id, String location, String threadnum, String timeout) {
        Class<?> boundType = null;
        try {
            boundType = Resources.classForName(location);
            if (boundType != null && AuthCodeProcessor.class.isAssignableFrom(boundType)) {
                if (!hasSchedule(id)) {
                    ScheduleExecutor executor = new ScheduleExecutor((AuthCodeProcessor)boundType.newInstance(), threadnum, timeout);
                    executor.start();
                    knownSchedules.put(id, executor);
                }
            }
        } catch (Exception e) {
        }
    }

    public boolean hasSchedule(String id){
        return knownSchedules.containsKey(id);
    }

    public ScheduleExecutor getSchedule(String id) {
        return knownSchedules.get(id);
    }
}
