package com.aoyun.crawlImg.kisspng;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;

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