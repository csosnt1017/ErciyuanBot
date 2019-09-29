package com.example.image;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * Copyright © 2019年 erciyuanboot. All rights reserved.
 *
 * @author 古今
 * <p>
 * xxxxx类
 * @date 2019/09/25
 * <p>
 * Modification History:
 * Date     Author    Version      Description
 * ---------------------------------------------------------*
 * 2019/09/25   古今   v1.0.0       新增
 */
public class HttpClientFactory {
    private static HttpClient httpClient;

    public static void buildHttpClient() {
        HttpHost httpHost = new HttpHost("127.0.0.1", 1080, "http");
        httpClient = HttpClients.custom().setProxy(httpHost).build();
    }

    public static HttpClient getHttpClient() {
        return httpClient;
    }
}