package com.aoyun.crawlImg;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class HttpUtil {
    private PoolingHttpClientConnectionManager cm;

    public HttpUtil(){
        this.cm = new PoolingHttpClientConnectionManager();
        //设置最大连接数
        this.cm.setMaxTotal(100);
        //设置每台host的最大连接数
        this.cm.setDefaultMaxPerRoute(10);
    }

    public String doGetHtml(String url){
        //通过连接词获取httpClient对象
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
        //通过创建httpGet对象，设置url对象
        HttpGet httpGet = new HttpGet(url);
        //设置请求信息
        httpGet.setConfig(this.getConfig());
        //使用httpClient发起请求，获取响应
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200){
                //判断响应体Entity是否为空，如果不为空就可以使用EntityUtils
                if (response.getEntity() != null){
                    return EntityUtils.toString(response.getEntity(),"utf8");
                }else {
                    return "响应体为空";
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //解析响应，返回结果
        return "error："+response.getStatusLine().getStatusCode();
    }


    public String doGetImage(String url){
        //通过连接词获取httpClient对象
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
        //通过创建httpGet对象，设置url对象
        HttpGet httpGet = new HttpGet(url);
        //设置请求信息
        httpGet.setConfig(this.getConfig());
        //使用httpClient发起请求，获取响应
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200){
                //判断响应体Entity是否为空，如果不为空就可以使用EntityUtils
                if (response.getEntity() != null){
                    //下载图片
                    //获取图片的后缀
                    String extName = url.substring(url.lastIndexOf("."));
                    //创建图片名，重命名图片
                    String picName = UUID.randomUUID().toString()+".jpg";
                    System.out.println("picName="+picName);
                    //下载图片
                    OutputStream outputStream = new FileOutputStream(new File("D:\\DicTim\\"+picName));
                    response.getEntity().writeTo(outputStream);
                    //返回图片名称
                    return picName;
                }else {
                    return "响应体为空";
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //解析响应，返回结果
        return "error："+response.getStatusLine().getStatusCode();
    }
    //设置请求信息
    private RequestConfig getConfig(){
        return RequestConfig.custom()
                .setSocketTimeout(1000)
                .setConnectionRequestTimeout(500)
                .setSocketTimeout(10000)
                .build();
    }


    public void downloadPicture(String urlList,String path) {
        URL url = null;
        try {
            url = new URL(urlList);
            DataInputStream dataInputStream = new DataInputStream(url.openStream());

            FileOutputStream fileOutputStream = new FileOutputStream(new File(path));
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int length;

            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            BASE64Encoder encoder = new BASE64Encoder();
            String encode = encoder.encode(buffer);//返回Base64编码过的字节数组字符串
            System.out.println(encode);
            fileOutputStream.write(output.toByteArray());
            dataInputStream.close();
            fileOutputStream.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}