package com.crawler.artifact.hander;

import java.util.Map;

/**
 * Created by liuzhixiong on 2018/9/25.
 * 动态参数处理
 */

public class ParameterHandler {

    /**
     * 动态参数类型为{num} 其中num与parameter数组下标对应
     * @param map
     * @param parameter
     */
    public static void setParameters(Map<String, String> map, String[] parameter) {

        for (Map.Entry<String, String> entry:map.entrySet()) {
            String value = entry.getValue();

            Integer[] matchNumbers = RegexUtil.matchs2Number(value);

            if(matchNumbers.length > 0){
                for (int val:matchNumbers) {
                    value = value.replace("{" + val + "}", parameter[val]);
                }
                entry.setValue(value);
            }
        }
    }

}
