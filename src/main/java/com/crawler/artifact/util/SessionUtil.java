package com.crawler.artifact.util;

import java.util.Random;

/**
 * Created by liuzhixiong on 2018/11/14.
 */
public class SessionUtil {
    public static String createSessionId() {
        return (getStringRandom(8) + "-" + getStringRandom(4) + "-" + getStringRandom(4) + "-" + getStringRandom(4)
                + "-" + getStringRandom(12)).toLowerCase();
    }

    public static String getStringRandom(int length) {
        String val = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            if ("char".equalsIgnoreCase(charOrNum)) {
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val = val + (char) (random.nextInt(26) + temp);
            } else if ("num".equalsIgnoreCase(charOrNum)) {
                val = val + String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }
}
