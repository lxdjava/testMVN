package com.aoyun.crawlImg.kisspng;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class KissPngUtil {
    //创建连接池管理器
    private static PoolingHttpClientConnectionManager httpPool = new PoolingHttpClientConnectionManager();
    //配置请求信息
    private static RequestConfig config = RequestConfig.custom().setConnectTimeout(2500)//设置连接的最长时间，单位是毫秒
            .setConnectionRequestTimeout(1500)//设置获取连接的最长时间
            .setSocketTimeout(20*1000)//设置传输的最长时间
            .build();

    public String doGetHtml(String url) throws Exception {
        //通过连接词获取httpClient对象
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(httpPool).build();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(config);
        httpGet.addHeader("cookie","HashMap<String, String> headers = new HashMap<String, String>();  \n" +
                "headers.put(\"Referer\", p.url);  \n" +
                "headers.put(\"User-Agent\", \"Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.6) Gecko/20100625   \n" +
                "  \n" +
                "Firefox/3.6.6 Greatwqs\");  \n" +
                "headers.put(\"Accept\",\"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\");  \n" +
                "headers.put(\"Accept-Language\",\"zh-cn,zh;q=0.5\");  \n" +
                "headers.put(\"Host\",\"www.yourdomain.com\");  \n" +
                "headers.put(\"Accept-Charset\",\"ISO-8859-1,utf-8;q=0.7,*;q=0.7\");  \n" +
                "headers.put(\"Referer\", \"http://www.yourdomian.com/xxxAction.html\");  \n" +
                "HttpRequestBase httpget = ......  \n" +
                "httpget.setHeaders(headers);");
        httpGet.setHeader("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36");
        httpGet.setHeader(":authority","www.kisspng.com");
        httpGet.setHeader(":path","/png-clip-art-portable-network-graphics-lilo-pelekai-tr-7308315/download-png.html");
        CloseableHttpResponse response = httpClient.execute(httpGet);
        if (response.getStatusLine().getStatusCode() == 200) {
            //判断响应体Entity是否为空，如果不为空就可以使用EntityUtils
            if (response.getEntity() != null) {
                return EntityUtils.toString(response.getEntity(), "utf8");

            }
        }
        return null;
    }


    public void doPost(String url){
        //从连接池管理器中拿到httpClient
        HttpClient httpClient = HttpClients.custom().setConnectionManager(httpPool).build();
        HttpPost httpPost = new HttpPost("url");
        httpPost.setConfig(config);
        URL url1 = null;

        //声明List集合，封装表单中的参数
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("p_k_n","java"));
        //创建表单的Entity对象,params是表单数据，"utf8"是编码
        UrlEncodedFormEntity formEntity = null;
        try {
            formEntity = new UrlEncodedFormEntity(params,"utf8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //设置表单但Entity对象到Post请求中
        httpPost.setEntity(formEntity);
    }
}