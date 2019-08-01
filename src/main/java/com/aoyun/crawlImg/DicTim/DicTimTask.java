package com.aoyun.crawlImg.DicTim;

import com.aoyun.crawlImg.HttpUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.UUID;

@Component
public class DicTimTask {
    private HttpUtil httpUtil = new HttpUtil();

    public static void main(String[] args) throws IOException {
        DicTimTask dicTimTask=new DicTimTask();
        dicTimTask.TicTask();
    }

    public void TicTask() throws IOException{
        //声明需要解析的初始地址
        String url = "https://www.sxxl.com/VectorGallery-index-cid-6-channel-2085-sex-32940.html?&p=";
        //按照页码对手机的搜索信息进行遍历爬取
        for (int i=1;i<467;i++){
            String html = null;
            try {
                html = httpUtil.doGetHtml(url + i);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //解析页面，获取商品数据并保存
            this.parse(html);
        }
    }
    //解析页面，获取数据
    private void parse(String html) throws IOException {
        //解析html获取DOM元素
        Document document = Jsoup.parse(html);
        //获取一整页商品
        Elements elements = document.select("a.list-img.showImg>img");
        URL url = null;
        for (Element element:elements){
            String src = element.attr("src");
            System.out.println(src);
            httpUtil.doGetImage(element.attr("src"));
        }
    }
}
