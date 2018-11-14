package com.crawler.artifact.http.environment;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by liuzhixiong on 2018/9/27.
 * 重试机制参数封装类
 */

public class RetryEntity {

    private int times = 1; //重试次数
    private List<Mean> means = new ArrayList<Mean>(); //失败判定策略,可以有多个策略

    public int getTimes() {
        return times;
    }

    public List<Mean> getMeans() {
        return means;
    }

    public void addMean(String type, String value){
        Mean mean = new Mean(type, value);
        this.means.add(mean);
    }


    public void setTimes(int times) {
        this.times = times;
    }

    public void setMeans(List<Mean> means) {
        this.means = means;
    }

    @Override
    public String toString() {
        return "RetryEntity{" +
                "times=" + times +
                ", means=" + means +
                '}';
    }

    /**
     * 重试失败判定策略
     */
    public class Mean {

        String type; //判定类型,如code表示根据Http响应码来判定
        String value; //判定值,如code为200表示成功,无需重试

        public Mean(String type, String value) {
            this.type = type;
            this.value = value;
        }

        public String getType() {
            return type;
        }

        public String getValue() {
            return value;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "Mean{" +
                    "type='" + type + '\'' +
                    ", value='" + value + '\'' +
                    '}';
        }
    }
}
