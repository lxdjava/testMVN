package com.aoyun.crawlImg.kisspng;

import com.aoyun.crawlImg.HttpUtil;

public class test {
    public static void main(String[] args) {
        HttpUtil httpUtil = new HttpUtil();
        try {
            String html = httpUtil.doGetHtml("https://www.kisspng.com/free/watercolor,1.html");
            System.out.println(html);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
