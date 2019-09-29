package com.example.image;


import com.alibaba.fastjson.JSONObject;
import jdk.nashorn.internal.parser.JSONParser;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright © 2019年 erciyuanboot. All rights reserved.
 *
 * @author 古今
 * <p>
 * xxxxx类
 * @date 2019/09/24
 * <p>
 * Modification History:
 * Date     Author    Version      Description
 * ---------------------------------------------------------*
 * 2019/09/24   古今   v1.0.0       新增
 */
public class Login {
    private final static String keyUrl = "https://accounts.pixiv.net/login?lang=zh&source=pc&view_type=page&ref=wwwtop_accounts_index";
    private final static String loginUrl = "https://accounts.pixiv.net/api/login?lang=zh";

    private static String postKey;
    private final static String pixivId = "1099261526@qq.com";
    private final static String password = "CSONLINE54o";

    private final static String cookies = "p_ab_id=4; p_ab_id_2=9; p_ab_d_id=1094367728; __utmc=235335808; " +
            "_ga=GA1.2.2031821681.1569313445; _gid=GA1.2.2099581816.1569313455; _ga=GA1.3.2031821681.1569313445; " +
            "_gid=GA1.3.2099581816.1569313455; login_bc=1; device_token=144dae7ca612074dc5aae0318acfe27e; " +
            "privacy_policy_agreement=1; c_type=22; a_type=0; b_type=1; " +
            "module_orders_mypage=%5B%7B%22name%22%3A%22sketch_live%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22tag_follow%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22recommended_illusts%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22everyone_new_illusts%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22following_new_illusts%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22mypixiv_new_illusts%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22spotlight%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22fanbox%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22featured_tags%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22contests%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22user_events%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22sensei_courses%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22booth_follow_items%22%2C%22visible%22%3Atrue%7D%5D; __utmv=235335808.|2=login%20ever=yes=1^3=plan=normal=1^5=gender=male=1^6=user_id=35260645=1^9=p_ab_id=4=1^10=p_ab_id_2=9=1^11=lang=zh=1; __utmz=235335808.1569315363.2.2.utmcsr=blog.csdn.net|utmccn=(referral)|utmcmd=referral|utmcct=/qq_28148007/article/details/91352688; __utma=235335808.2031821681.1569313445.1569315363.1569379799.3; tag_view_ranking=jfnUZgnpFl~ZISI5v0br2~CvNLjkK_C2~SQZaakhtVv~WrPRDtgA7s~qoCYB4uOWI~TMR0WAh90w~_6RRluE2GM~tlI9YiBhjp~DHqIUplIEc~mHukPa9Swj~AL_Ixyn11N~eiUfZVqVq1~5mzv1EsHcE~yPNaP3JSNF~THNBMXb9GO~Cj_Gcw9KR1~drSdF7vT3W; __utmt=1; __utmb=235335808.19.9.1569383139533; PHPSESSID=4c6e2818bd7a9f5348f920692a590f42; _gat=1; _gat_UA-76252338-4=1";

    public static void getPostKey() throws IOException {
        HttpGet httpGet = new HttpGet(keyUrl);

        httpGet.setHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                "(KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36");

        HttpResponse httpResponse = HttpClientFactory.getHttpClient().execute(httpGet);
        String parseXml = EntityUtils.toString(httpResponse.getEntity());
        Document document = Jsoup.parse(parseXml);
        String val = document.getElementById("init-config").val();
        JSONObject jsonObject = JSONObject.parseObject(val);
        postKey = (String) jsonObject.get("pixivAccount.postKey");
        if (postKey == null) {
            throw new RuntimeException("postKey获取失败");
        }

    }



    public static void login() throws IOException {
        HttpPost httpPost = new HttpPost(loginUrl);
        List<NameValuePair> nameValuePairList = new ArrayList<>();
        nameValuePairList.add(new BasicNameValuePair("pixiv_id", pixivId));
        nameValuePairList.add(new BasicNameValuePair("password", password));
        nameValuePairList.add(new BasicNameValuePair("post_key", postKey));

        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairList, Charset.forName("UTF-8")));
        httpPost.setHeader("cookie",cookies);
        HttpResponse response = HttpClientFactory.getHttpClient().execute(httpPost);

        String xml = EntityUtils.toString(response.getEntity(), Charset.forName("UTF-8"));
        JSONObject jsonObject = JSONObject.parseObject(xml);
        if (jsonObject.get("error") != null) {
            System.out.println(jsonObject.toJSONString());
            throw new RuntimeException("登录失败");
        }

    }



}