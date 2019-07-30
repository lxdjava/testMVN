package com.aoyun.crawlImg;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

@Component
public class DicTimTask {
    private HttpUtil httpUtil = new HttpUtil();

    public static void main(String[] args) throws IOException {
        DicTimTask dicTimTask=new DicTimTask();
        dicTimTask.jdTask();
    }

    //每当下载任务完成后，间隔多长时间进行下一次的任务
    public void jdTask() throws IOException{
        //声明需要解析的初始地址
        String url = "https://www.sxxl.com/VectorGallery-index-cid-6-channel-2085-sex-32940.html?&p=";
        //按照页码对手机的搜索信息进行遍历爬取
        for (int i=1;i<467;i++){
            String html = httpUtil.doGetHtml(url + i);
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