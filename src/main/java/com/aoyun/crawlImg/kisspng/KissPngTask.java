package com.aoyun.crawlImg.kisspng;

import com.aoyun.crawlImg.HttpUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;

public class KissPngTask {
    private static HttpUtil httpUtil = new HttpUtil();
    // 创建等待队列
    /*//private static BlockingQueue bqueue = new ArrayBlockingQueue(100);
    private static LinkedBlockingQueue linkedBlockingQueue = new LinkedBlockingQueue();
    // 创建一个单线程执行程序，它可安排在给定延迟后运行命令或者定期地执行。
    private static ThreadPoolExecutor pool = new ThreadPoolExecutor(5, 5, 0, TimeUnit.MILLISECONDS, linkedBlockingQueue);*/

    public static void main(String[] args) {
        KissPngTask();
    }
    public static void KissPngTask() {
        //网站主页
        String index = "https://www.kisspng.com";
        //要将图片下载到哪个文件夹
        File file = new File("F:\\kisspng.com");
        parse(index, file);
    }
    private static void parse(String url, File file) {
        //解析html获取DOM元素
        String html = null;
        try {
            html = httpUtil.doGetHtml(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Document document = Jsoup.parse(html);
        //从一级分类开始解析
        Elements elements = document.select("ul.header-top-ul a");
        for (Element element:elements){
            String ctg = element.text();
            File ctgFile = new File(file.getPath()+"\\"+ctg);
            if (!ctgFile.exists()){
                ctgFile.mkdir();
            }
            //解析分类下的60页图片
            ctgDown(url+"/free/"+ctg.toLowerCase(),ctgFile);
            break;
        }
    }

    private static void ctgDown(String url,File dir){
        String pageUrl = null;
        for (int i = 1; i<61; i++){
            //解析html获取DOM元素
            pageUrl = url+","+i+".html";
            String html = null;
            try {
                html = httpUtil.doGetHtml(pageUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Document document = Jsoup.parse(html);
            Elements elements = document.select("div.list-tree-btns");
            for (Element element : elements){
                String preview = "https://www.kisspng.com" + element.select("a").first().attr("href") + "preview.html";
                System.out.println(preview);
                String previewHtm = null;
                try {
                    previewHtm = httpUtil.doGetHtml(preview);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Document previewDom = Jsoup.parse(previewHtm);
                Elements original_img = previewDom.select("div#original_img");
                System.out.println(original_img);
                break;
            }
            break;
        }
    }
}
