package com.aoyun.crawlImg.ssyer;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ssyUtil {
    public static void main(String[] args) {
        doPostPage("1");
    }
    public static String doPostPage(String url) {
        // 获取连接客户端工具
        CloseableHttpClient httpClient = HttpClients.createDefault();

        String entityStr = null;
        CloseableHttpResponse response = null;

        try {
            HttpPost httpPost = new HttpPost("https://www.ssyer.com/apis/20301");
            /** 第一种添加参数的形式 */
            List<NameValuePair> list = new LinkedList<NameValuePair>();
            list.add(new BasicNameValuePair("id","875072"));
            list.add(new BasicNameValuePair("width","0"));
            httpPost.setEntity(new UrlEncodedFormEntity(list, Charset.forName("UTF-8")));
            /*
             * 添加请求头信息
             */
            // 传输的类型
            httpPost.setHeader("Accept","application/json");
            httpPost.setHeader("Accept-Encoding","gzip, deflate, br");
            httpPost.setHeader("Accept-Language","zh-CN,zh;q=0.9");
            httpPost.setHeader("Connection:","keep-alive");
            //httpPost.addHeader("Content-Length","23");
            httpPost.setHeader("Content-Type", "application/json");
            //cookie
            String cookies=
                "_dg_playback.7b6028a56aac520d.ce42=1; _dg_abtestInfo.7b6028a56aac520d.ce42=1; _dg_check.7b6028a56aac520d.ce42=-1; Hm_lvt_8f50334c83664955c1a1a866dd168053=1565052068,1565060219,1565069373,1565138588; Hm_lpvt_8f50334c83664955c1a1a866dd168053=1565138588; SESSION=YTg1MjVkNjctMWIxMy00YjIxLThjZDItNjdlZThlZjU1YmY3; uid=118905; _dg_id.7b6028a56aac520d.ce42=170a9a84a5644cf7%7C%7C%7C1564981066%7C%7C%7C0%7C%7C%7C1565138615%7C%7C%7C1565138611%7C%7C%7C%7C%7C%7Cdb072c84526bd5dc%7C%7C%7C%7C%7C%7C%7C%7C%7C0%7C%7C%7Cundefined";
            httpPost.setHeader("Cookie", cookies.replace(" ",""));
            //host
            httpPost.setHeader("Host","www.ssyer.com");
            //Origin
            httpPost.setHeader("Origin:","https://www.ssyer.com");
            //referer
            httpPost.setHeader("Referer","https://www.ssyer.com/cate/3");
            // 浏览器表示
            httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36");
            // 执行请求
            response = httpClient.execute(httpPost);
            // 获得响应的实体对象
            HttpEntity responseEntity = response.getEntity();
            // 使用Apache提供的工具类进行转换成字符串
            entityStr = EntityUtils.toString(responseEntity, "UTF-8");
            System.out.println(entityStr);
            System.out.println(list);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
