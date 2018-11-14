package com.crawler.artifact.session.test;

import com.crawler.artifact.service.TbSVC;
import com.crawler.artifact.service.CrawlerResult;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * Created by liuzhixiong on 2018/10/15.
 */
public class TbTest extends BaseTestCase {

    @Resource
    TbSVC tbSVC;

    @Test
    public void test(){

        String sessionID = "123456";

        CrawlerResult crawlerResult = tbSVC.login(sessionID);
        System.out.println("crawlerResult=" + crawlerResult);

    }

}
