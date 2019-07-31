package com.aoyun.crawlImg;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectionPoolTimeoutException;
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
    private static volatile FileOutputStream finished;
    private static volatile FileOutputStream existed;
    private static volatile FileOutputStream entituNull;
    private static volatile FileOutputStream ioException;
    private static volatile FileOutputStream responseClose;
    private static volatile FileOutputStream exception;

    static {
        try {
            finished = new FileOutputStream(new File("D:\\爬虫\\pngimg.com\\log\\finished.txt"));
            existed = new FileOutputStream(new File("D:\\爬虫\\pngimg.com\\log\\existed.txt"));
            entituNull = new FileOutputStream(new File("D:\\爬虫\\pngimg.com\\log\\entity_null.txt"));
            ioException = new FileOutputStream(new File("D:\\爬虫\\pngimg.com\\log\\io_exception.txt"));
            responseClose = new FileOutputStream(new File("D:\\爬虫\\pngimg.com\\log\\response_close.txt"));
            exception = new FileOutputStream(new File("D:\\爬虫\\pngimg.com\\log\\exception.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public HttpUtil() {
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
        HttpGet httpGet = null;
        try {
            httpGet = new HttpGet(url);
        }catch (Exception e){
            try {
                exception.write(("地址"+url+"不存在").getBytes());
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
        }

        //设置请求信息
        try {
            httpGet.setConfig(this.getConfig());
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

        //使用httpClient发起请求，获取响应
        CloseableHttpResponse response = null;
        try {
            try {
                //这里有可能连接超时
                response = httpClient.execute(httpGet);
            }catch (Exception e){
                exception.write((url+e.getMessage()).getBytes());
                return null;
            }

            if (response.getStatusLine().getStatusCode() == 200){
                //判断响应体Entity是否为空，如果不为空就可以使用EntityUtils
                if (response.getEntity() != null){
                    return EntityUtils.toString(response.getEntity(),"utf8");
                }else {
                    return null;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        //解析响应，返回结果
        return "error："+response.getStatusLine().getStatusCode();
    }


    public String doGetImage(String url){
        //通过连接池获取httpClient对象
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
            return null;
        }finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        //解析响应，返回结果
        return "error："+response.getStatusLine().getStatusCode();
    }
    //设置请求信息
    private RequestConfig getConfig(){
        return RequestConfig.custom()
                //从连接池中获取连接的超时时间
                .setConnectionRequestTimeout(3000)
                .setConnectTimeout(5000)
                .setSocketTimeout(25000)
                .build();
    }

    public void doGetPngImg(String url,File file) {
        //通过连接池获取httpClient对象
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
        //通过创建httpGet对象，设置url对象
        HttpGet httpGet = new HttpGet(url);
        //设置请求信息
        httpGet.setConfig(this.getConfig());
        //使用httpClient发起请求，获取响应
        CloseableHttpResponse response = null;
        //获取图片名
        String picName = url.substring(url.lastIndexOf("/"));
        try {
            response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200){
                //判断响应体Entity是否为空，如果不为空就可以使用EntityUtils
                if (response.getEntity() != null){
                    //下载图片
                    File newFile = new File(file+"\\"+picName);
                    if (newFile.exists()){
                        /*existed.write((picName+"\r\n").getBytes());*/
                        return;
                    }
                    OutputStream outputStream = new FileOutputStream(newFile);
                    response.getEntity().writeTo(outputStream);
                    outputStream.close();
                    //记录日志
                    finished.write((picName+"\r\n").getBytes());
                }else {
                    entituNull.write((picName+"\r\n").getBytes());
                }
            }
        } catch (IOException e) {
            try {
                ioException.write((picName+"\r\n").getBytes());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }finally {
            try {
                response.close();
            } catch (IOException e) {
                try {
                    responseClose.write((picName+"\r\n").getBytes());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}