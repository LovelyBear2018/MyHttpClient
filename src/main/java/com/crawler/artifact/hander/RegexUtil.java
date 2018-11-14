package com.crawler.artifact.hander;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by liuzhixiong on 2018/9/25.
 * 正则表达式工具类
 */

public class RegexUtil {

    private static Pattern p = Pattern.compile("\\{(\\d*)\\}");


    /**
     * 匹配{数字} 并将匹配值转为整形数组
     * @param seq
     * @return
     */
    public static Integer[] matchs2Number(String seq) {

        List<Integer> resList = new ArrayList<Integer>();
        Matcher m = p.matcher(seq);
        while(m.find()){
            resList.add(Integer.valueOf(m.group(1)));
        }
        return resList.toArray(new Integer[resList.size()]);

    }

}
