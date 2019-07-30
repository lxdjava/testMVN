package com.aoyun.crawlImg.pngimg;

import com.aoyun.crawlImg.HttpUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.*;

public class PngImgTaks {
    private static HttpUtil httpUtil = new HttpUtil();
    private static PngImgPojo pngImgPojo = new PngImgPojo();

    public static void main(String[] args) throws IOException {
        PngImgTaks pngImgTaks = new PngImgTaks();
        pngImgTaks.TicTask();
    }

    public void TicTask() throws IOException {
        //声明需要解析的初始地址
        String url = "http://pngimg.com/";
        //要将图片下载到哪个文件夹
        File file = new File("H:\\爬虫\\pngimg.com");
        this.parse(url, file);

    }
    //解析页面，获取数据
    private void parse(String url, File file) throws IOException {
        //解析html获取DOM元素
        String html = httpUtil.doGetHtml(url);
        Document document = Jsoup.parse(html);
        //从一级分类开始解析
        Elements elements = document.select("li.catalog");
        for (Element element : elements){
            //获取一级分类
            Elements categorys = element.select(".category a");
            String category = categorys.text();
            File file1 = new File(file.getPath()+"\\"+category);
            if (!file1.exists()){
                file1.mkdir();
            }
            //获取二级分类
            Elements sub_categorys = element.select(".sub_category a");
            for (Element element1 : sub_categorys){
                String sub_category = element1.text();
                File file2 = new File(file1.getPath()+"\\"+sub_category);
                if (!file2.exists()){
                    file2.mkdir();
                }
                String sub_category_url = url+"imgs/"+category+"/"+sub_category+"/";
                System.out.println("====================二级分类地址："+sub_category_url.toLowerCase()+"；开始爬取=========================");
                //进入二级分类，去一张张获取图片
                sub_ctg(sub_category_url.toLowerCase(),file2);
                break;
            }
            break;

        }
    }

    // 创建等待队列
    private static BlockingQueue bqueue = new ArrayBlockingQueue(20);
    // 创建一个单线程执行程序，它可安排在给定延迟后运行命令或者定期地执行。
    private static ThreadPoolExecutor pool = new ThreadPoolExecutor(2, 3, 2, TimeUnit.MILLISECONDS, bqueue);

    private static void sub_ctg(String  url, File file2){
        String html = httpUtil.doGetHtml(url);
        Document document = Jsoup.parse(html);
        Elements imgs = document.select("div.png_png.png_imgs>a");
        for (Element img : imgs){
            String src = "http://pngimg.com"+img.select("img").attr("src");
            System.out.println(src);
            pool.execute(new SubThread(src,file2));
            //httpUtil.doGetPngImg(src, file2);
            //break;
        }
    }


    static class SubThread implements Runnable {
        private String src;
        private File file;
        public SubThread(String s,File f) {
            this.src=s;
            this.file=f;
        }

        @Override
        public void run() {
            httpUtil.doGetPngImg(src, file);
        }
    }
}