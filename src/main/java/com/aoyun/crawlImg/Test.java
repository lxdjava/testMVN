package com.aoyun.crawlImg;

public class Test {
    public static void main(String[] args) {
        HttpUtil httpUtil = new HttpUtil();
        //httpUtil.doGetHtml("http://youchaikj.com/imageThumbnail/2ff806c5-80bc-4f34-98ed-2718b761d507.png");
        httpUtil.downloadPicture("","C:\\Users\\Administrator\\Desktop\\DicTim\\1.jpg");
    }
}
