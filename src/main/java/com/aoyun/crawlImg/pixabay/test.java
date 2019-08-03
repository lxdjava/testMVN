package com.aoyun.crawlImg.pixabay;

import com.aoyun.crawlImg.HttpUtil;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;

import java.io.File;

public class test {
    private static HttpUtil httpUtil = new HttpUtil();
    public static void main(String[] args) {
        imgHtml("https://pixabay.com/vectors/abstract-art-decorative-floral-1300237/",new File("D:\\works\\IdeaWorkSpace\\testMVN\\src"));
    }
    public static void imgHtml(String url, File file2) {
        String html = null;
        html = httpUtil.doGetHtml(url);
        if (StringUtil.isBlank(html)){
            System.out.println(url+"这个url获取不到html");
            return;
        }
        Document document = Jsoup.parse(html);
        String imgUrl = document.select("[itemprop=contentURL]").attr("src");
        System.out.println(imgUrl);
        httpUtil.doGetPngImg(imgUrl, file2);
        //System.out.println(imgUrl + ":" + file2);
    }
}
