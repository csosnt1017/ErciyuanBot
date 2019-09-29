package com.example.image;

import com.alibaba.fastjson.JSONObject;
import com.example.model.PixivImgRankDO;
import com.sobte.cqp.jcq.util.DigestUtils;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class DownLoadPic {
    // 编码
    private static final String ECODING = "UTF-8";
    // 获取img标签正则
    private static final String IMGURL_REG = "<img.*src=(.*?)[^>]*?>";
    // 获取src路径的正则
    private static final String IMGSRC_REG = "http:\"?(.*?)(\"|>|\\s+)";

    private static final String RANKURL_PREFIX = "https://www.pixiv.net/ranking.php?";

    private static final String RANKURL_SUFFIX = "&format=json";

    private static final String DETAILIMGURL_PREFIX = "https://www.pixiv.net/ajax/illust/";


    /***
     * 下载图片
     *
     */
    public static String download(String imgSrc, String requestUrl) {
        String imageName = null;
        try {
            imageName = imgSrc.substring(imgSrc.lastIndexOf("/") + 1);

            URL uri = new URL(imgSrc);
            URLConnection urlConnection = uri.openConnection();
            urlConnection.setRequestProperty("Referer", requestUrl);
            InputStream in = urlConnection.getInputStream();
            CQImage image = new CQImage(imgSrc, in, imageName, urlConnection.getContentLength());

            Map<String, List<String>> stringListHashMap = new HashMap<>(1);
            List<String> headsList = new ArrayList<>();
            headsList.add(requestUrl);
            stringListHashMap.put("Referer", headsList);

            File path = image.downloadUseGet(new File("data/image/", image.getName()), stringListHashMap);
            path.deleteOnExit();
//            FileOutputStream fo = new FileOutputStream();
//            byte[] buf = new byte[1024];
//            int length = 0;
//            while ((length = in.read(buf, 0, buf.length)) != -1) {
//                fo.write(buf, 0, length);
//            }
            in.close();
//            fo.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return imageName;
    }

    /**
     * @Description: 每日排行
     * @return:
     * @Author: gj
     * @Date: 2019/9/25
     */
    public static List<PixivImgRankDO> listImageUrl(String cookie, String url) {
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("cookie", cookie);
        httpGet.setHeader("Referer", url);
        HttpResponse httpResponse = null;
        String responseBody = null;
        try {
            httpResponse = HttpClientFactory.getHttpClient().execute(httpGet);
            responseBody = EntityUtils.toString(httpResponse.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage().contains("Connection refused: connect")) {
                throw new RuntimeException("喵帕斯翻不出去啦！");
            }
        }
        JSONObject jsonObject = JSONObject.parseObject(responseBody);
        if (jsonObject.get("error") != null) {
            throw new RuntimeException((String) jsonObject.get("error"));
        }
        List<Map<String, Object>> jsonObjectList = (List<Map<String, Object>>) jsonObject.get("contents");
        List<PixivImgRankDO> imgRankDOList = new ArrayList<>();
        jsonObjectList.forEach(j -> {
            String illustId = ((Integer) j.get("illust_id")).toString();
            String userId = ((Integer) j.get("user_id")).toString();
            String userName = (String) j.get("user_name");
            String title = (String) j.get("title");
            String detailImgUrl = getImageUrl(cookie, getDetailUrl(null, illustId));
            PixivImgRankDO pixivImgRankDO = new PixivImgRankDO(userId, userName, illustId, title, detailImgUrl);
            imgRankDOList.add(pixivImgRankDO);
        });
        return imgRankDOList;
    }

    public static String getImageUrl(String cookie, String url) {
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("cookie", cookie);
        httpGet.setHeader("Referer", url);
        HttpResponse httpResponse = null;
        String responseBody = null;
        try {
            httpResponse = HttpClientFactory.getHttpClient().execute(httpGet);
            responseBody = EntityUtils.toString(httpResponse.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage().contains("Connection refused: connect")) {
                throw new RuntimeException("喵帕斯翻不出去啦！");
            }
        }
        JSONObject jsonObject = JSONObject.parseObject(responseBody);
        if (jsonObject.get("error") != null) {
            if ((Boolean) jsonObject.get("error")) {
                throw new RuntimeException((String) jsonObject.get("message"));
            }
        }
        JSONObject jsonObjectBody = (JSONObject) jsonObject.get("body");
        Map<String, String> urlsMap = (Map<String, String>) jsonObjectBody.get("urls");
        return urlsMap.get("original");
    }

    /**
     * @Description: 获取每日排行url
     * @return:
     * @Author: gj
     * @Date: 2019/9/25
     */
    public static String getRankUrl(String page, String mode, String content) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(RANKURL_PREFIX);
        if (mode != null) {
            stringBuffer.append("mode=" + mode);
        }
        if (content != null) {
            stringBuffer.append("&content=" + content);
        }
        if (page != null) {
            stringBuffer.append("&p=" + page);
        }
        stringBuffer.append(RANKURL_SUFFIX);
        return stringBuffer.toString();
    }


    /**
     * @Description: 获取作品详情url
     * @return:
     * @Author: gj
     * @Date: 2019/9/25
     */
    public static String getDetailUrl(String content, String workId) {
        return DETAILIMGURL_PREFIX + workId;
    }
}