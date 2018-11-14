package com.crawler.artifact.session.test;

import com.crawler.artifact.service.CrawlerResult;
import com.crawler.artifact.service.InfoBean;
import com.crawler.artifact.service.JdSVC;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * Created by liuzhixiong on 2018/9/20.
 */

public class JdTest extends BaseTestCase{

    @Resource
    JdSVC jdSVC;

    @Test
    public void test(){

        String sessionID = "123456";
        InfoBean infoBean = new InfoBean();
        infoBean.setSessionId(sessionID);
        for(int i=0; i<1; i++){
            CrawlerResult crawlerResult = jdSVC.jd_login(infoBean);
            System.out.println("crawlerResult=" + crawlerResult);
        }
    }

}

