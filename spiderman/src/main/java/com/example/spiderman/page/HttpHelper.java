package com.example.spiderman.page;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class HttpHelper {

    public static Page  sendRequstAndGetResponse(String url, String proxy) throws IOException {
        Page page = null;
        String[] proxyStr = proxy.split(":");
        String host = proxyStr[0];
        String port = proxyStr[1];

        // 创建httpget实例
        HttpGet httpGet=new HttpGet(url);
        CloseableHttpClient client = setProxy(httpGet, host, Integer.parseInt(port));
        //设置请求头消息
        httpGet.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.110 Safari/537.36");

        // 执行http get请求  也可以使用psot
        CloseableHttpResponse response=client.execute(httpGet);
        // 获取返回实体
        if (response != null){
            HttpEntity entity = response.getEntity();
            if (entity != null){
                page = new Page(entity.getContent().toString().getBytes(), url, entity.getContentType().toString());
                System.out.println("网页内容为:"+ EntityUtils.toString(entity,"utf-8"));
            }
        }
        //关闭response
        response.close();
        //关闭httpClient
        client.close();

        return page;
    }

    public static CloseableHttpClient setProxy(HttpGet httpGet,String proxyIp,int proxyPort){
        // 创建httpClient实例
        CloseableHttpClient httpClient= HttpClients.createDefault();
        //设置代理IP、端口
        HttpHost proxy=new HttpHost(proxyIp,proxyPort,"http");
        //也可以设置超时时间   RequestConfig requestConfig = RequestConfig.custom().setProxy(proxy).setConnectTimeout(3000).setSocketTimeout(3000).setConnectionRequestTimeout(3000).build();
        RequestConfig requestConfig=RequestConfig.custom().setProxy(proxy).build();
        httpGet.setConfig(requestConfig);
        return httpClient;
    }
}
