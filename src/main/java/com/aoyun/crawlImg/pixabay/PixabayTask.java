package com.aoyun.crawlImg.pixabay;

import com.aoyun.crawlImg.Finish;
import com.aoyun.crawlImg.HttpUtil;
import org.apache.commons.codec.binary.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PixabayTask {
    private static HttpUtil httpUtil = new HttpUtil();
    private static File rootDir = new File("F:\\pixabay.com");
    private static String url = "https://pixabay.com/illustrations/search/";
    //private static BlockingQueue bqueue = new ArrayBlockingQueue(100);
    private static LinkedBlockingQueue linkedBlockingQueue = new LinkedBlockingQueue();
    // 创建一个单线程执行程序，它可安排在给定延迟后运行命令或者定期地执行。
    private static ThreadPoolExecutor pool = new ThreadPoolExecutor(6, 6, 0, TimeUnit.MILLISECONDS, linkedBlockingQueue);

    public static void main(String[] args) {
        ctgForeach();
    }
    //遍历分类
    public static void ctgForeach(){
        Map<String, List<String>> category = Category.Category();
        for (Map.Entry<String, List<String>> entry:category.entrySet()){
            String ctg1 = entry.getKey();
            File file1 = new File(rootDir.getPath()+"/"+ctg1);
            //管理分类
            if (ctg1.equals("2020")||ctg1.equals("Terrestrial animals")){
                System.out.println("跳过分类："+ctg1);
                continue;
            }
            System.out.println("开始==============================================================="+ctg1);
            if (!file1.exists()){
                file1.mkdir();
            }
            for (String ctg2:entry.getValue()){
                File file2 = new File(rootDir.getPath()+"/"+ctg1+"/"+ctg2);
                //不要搜other
                if (ctg2.equals("Other")){
                    continue;
                }
                if (!file2.exists()){
                    file2.mkdir();
                }
                ctgSearch(ctg2,file2);
                System.out.println("-------------------------------"+ctg2);
//                break;
            }
            System.out.println("解析完成==============================================================="+ctg1);
//            break;
        }
        pool.shutdown();
        Finish.print();
    }

    public static void ctgSearch(String ctg, File file2) {
        String replace = ctg.replace(" ", "%20");
        String serachURl = url+replace;
        System.out.println(serachURl);
        //访问这个分类搜索，然后获取分页页数
        String html = null;
        try {
             html = httpUtil.doGetHtml(serachURl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Document document = Jsoup.parse(html);
        String pageMaxString = document.select(".paginator").text();
        if (StringUtil.isBlank(pageMaxString)){
            return;
        }
        int pageMax = getPageMax(pageMaxString);
        //分页循环
        for (int i = 1; i<=pageMax; i++){
            pageHtml(serachURl+"/?pagi="+i,file2);
//            break;
        }
    }
    //获取总页数
    public static int getPageMax(String pageMax){
        String regEx="[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(pageMax);
        int trim = 0;
        try {
            trim = Integer.parseInt(m.replaceAll("").trim());
        }catch (Exception e){
            e.printStackTrace();
        }
        return trim;
    }
    //解析每个搜索页的h5代码
    public static void pageHtml(String url, File file2){
        String html = null;
        try {
            html = httpUtil.doGetHtml(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Document document = Jsoup.parse(html);
        Elements a = document.select(".flex_grid>.item>a");
        //遍历a标签列表
        for (Element element : a){
            String s = element.attr("href");
            String imgUrl = "https://pixabay.com"+s;
            //开启线程,这个线程计算较少，都是io操作
            pool.execute(new SubThread(imgUrl,file2));
//            break;
        }
    }
    //多线程，负责解析单页图片以及下载图片
    static class SubThread implements Runnable {
        private String src;
        private File file;
        public SubThread(String s,File f) {
            this.src=s;
            this.file=f;
        }

        @Override
        public void run() {
            imgHtml(src,file);
        }
    }
    //解析每个图片h5代码
    public static void imgHtml(String url, File file2) {
        String html = null;
        html = httpUtil.doGetHtml(url);
        if (StringUtil.isBlank(html)){
            System.out.println(url+"这个url获取不到html");
            return;
        }
        Document document = Jsoup.parse(html);
        String imgUrl = document.select("[itemprop=contentURL]").attr("src");
        httpUtil.doGetPngImg(imgUrl, file2);
    }
}