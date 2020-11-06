package com.zhch.paysvc.utils;

import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.InputStream;

@Log4j2
public class HttpClientSupport {


    public static String doPost(String url, String jsonStr, String charset) throws Exception {
        HttpClient httpClient;
        HttpPost httpPost;
        String result = null;
        httpClient = HttpClients.createDefault();
        httpPost = new HttpPost(url);
        StringEntity entity = new StringEntity(jsonStr, charset);
        entity.setContentEncoding(
                new BasicHeader(HTTP.CONTENT_TYPE, HttpConfigEnum.APPLICATION_JSON.getName()));
        httpPost.setEntity(entity);
        HttpResponse response = httpClient.execute(httpPost);
        if (response != null) {
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                result = EntityUtils.toString(resEntity, charset);
            }
            EntityUtils.consume(resEntity);
        }
        return result;
    }

    public static InputStream doGet(String url, String charset) throws Exception {
        HttpClient httpClient;
        HttpGet httpGet;
        InputStream result = null;
        httpClient = HttpClients.createDefault();
        httpGet = new HttpGet(url);
        HttpResponse response = httpClient.execute(httpGet);
        if (response != null) {
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                result = resEntity.getContent();
            }
            EntityUtils.consume(resEntity);
        }

        return result;
    }

    public static String doGetStringResult(String url, String charset) {
        HttpClient httpClient;
        HttpGet httpGet;
        String result = null;
        try {
            httpClient = HttpClients.createDefault();
            httpGet = new HttpGet(url);
            HttpResponse response = httpClient.execute(httpGet);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, charset);
                }
                EntityUtils.consume(resEntity);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error(ex.getMessage(), ex);
        }
        return result;
    }


    public static String internalDoPost(String url, String jsonStr, String charset) {
        HttpClient httpClient;
        HttpPost httpPost;
        String result = null;
        try {
            httpClient = HttpClients.createDefault();
            httpPost = new HttpPost(url);
            StringEntity entity = new StringEntity(jsonStr);
            entity.setContentType(HttpConfigEnum.CONTENT_TYPE.getName());
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, charset);
                }
                EntityUtils.consume(resEntity);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error(ex.getMessage(), ex);
        }
        return result;
    }

}