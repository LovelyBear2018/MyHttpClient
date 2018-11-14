package com.crawler.artifact.service;

import com.crawler.artifact.binding.AuthCodeCacheBean;
import com.crawler.artifact.http.httpclient.AuthCodeProcessor;
import com.crawler.artifact.http.httpclient.HttpCrawler;
import com.crawler.artifact.http.httpclient.Response;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuzhixiong on 2018/10/22.
 */
public class MFAuthCodeProcessor implements AuthCodeProcessor{

    @Override
    public AuthCodeCacheBean buildAuthCodeCacheBean(){

        AuthCodeCacheBean authCodeCacheBean = new AuthCodeCacheBean();
        String sartUrl ="https://uia.xiamenair.com/external/api/v1/oauth2/authorize?scope=user&response_type=code&redirect_uri=https://www.xiamenair.com/zh-cn/Login/Login&client_id=PCWEB&lang=zh_cn";
        String imageUrl = "https://uia.xiamenair.com/external/api/v1/oauth2/captcha";

        HttpCrawler lc = new HttpCrawler();

        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        headers.put("Accept-Encoding", "gzip, deflate, br");
        headers.put("Accept-Language", "zh-CN,zh;q=0.8");
        headers.put("Connection", "keep-alive");
        headers.put("Host", "uia.xiamenair.com");
        headers.put("Referer", "https://www.xiamenair.com/zh-cn/Home/Index");
        headers.put("Upgrade-Insecure-Requests", "1");
        headers.put("User-Agent",
                "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Mobile Safari/537.36");

        Response resp = lc.get(sartUrl, headers, "utf-8");

        String content = resp.getContent();
        Document doc = Jsoup.parse(content);
        String pageToken = doc.getElementsByAttributeValue("class", "pageToken").attr("value");

        if(resp.getStatus().getStatecode() == 200){
            byte[] imageByteArray = lc.getImage(imageUrl).getBufferedImage();
            if(imageByteArray != null && StringUtils.isNotBlank(pageToken)){
                authCodeCacheBean.setHc(lc);
                Map<String, String> parameter = new HashMap<String, String>();
                parameter.put("pageToken", pageToken);
                authCodeCacheBean.setImage(imageByteArray);
                authCodeCacheBean.setParameter(parameter);
                return authCodeCacheBean;
            }
        }

        return null;
    }
}
