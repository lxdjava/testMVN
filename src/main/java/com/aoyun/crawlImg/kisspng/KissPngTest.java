package com.aoyun.crawlImg.kisspng;


import com.aoyun.crawlImg.HttpUtil;

public class KissPngTest {
    private static HttpUtil httpUtil = new HttpUtil();
    private static KissPngUtil kissPngUtil = new KissPngUtil();
    public static void main(String[] args) {
        //String html = httpUtil.doGetHtml("https://www.kisspng.com/png-clip-art-portable-network-graphics-lilo-pelekai-tr-7308315/download-png.html");
        String html1 = null;
        try {
            html1 = kissPngUtil.doGetHtml("https://www.kisspng.com/png-clip-art-portable-network-graphics-lilo-pelekai-tr-7308315/download-png.html");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(html1);

    }
}
