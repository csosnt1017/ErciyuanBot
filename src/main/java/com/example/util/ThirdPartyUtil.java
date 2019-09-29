package com.example.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.model.TopNews;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * Copyright © 2019年 erciyuanboot. All rights reserved.
 *
 * @author 古今
 * <p>
 * xxxxx类
 * @date 2019/09/11
 * <p>
 * Modification History:
 * Date     Author    Version      Description
 * ---------------------------------------------------------*
 * 2019/09/11   古今   v1.0.0       新增
 */
public class ThirdPartyUtil {
    public static String excuteWeather(String cityName) {
        String url = "http://v.juhe.cn/weather/index?cityname=" + cityName + "&dtype=&format=&key=953b3150eb5ee1cdfd633b97961a50e5";//接口URL
        //PureNetUtil是一个封装了get和post方法获取网络请求数据的工具类
        return PureNetUtil.get(url);//使用get方法
    }

    public static String excuteTopNews() {
        String url = "http://v.juhe.cn/toutiao/index?type=&key=19c00b78a82e25e0be7970b1dd53e1f1";//接口URL
        //PureNetUtil是一个封装了get和post方法获取网络请求数据的工具类
        return PureNetUtil.get(url);//使用get方法
    }

    public static String getWeather(String msg) {
        String cityName = msg.replace("天气", "");
        try {
            cityName = URLEncoder.encode(cityName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = excuteWeather(cityName);//返回接口结果,得到json格式数据
        if (result != null) {
            JSONObject obj = JSONObject.parseObject(result);
            result = obj.getString("resultcode");//得到返回状态码
            if (result != null && result.equals("200")) {//200表示成功返回数据
                Map<String, Map> resultMap = (Map) obj.get("result");
                Map todayWeather = resultMap.get("today");
                if (todayWeather != null) {
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append("喵帕斯为您播报今日" + todayWeather.get("city").toString() + "天气：\n\n");
                    stringBuffer.append("城市：" + todayWeather.get("city").toString() + "\n");
                    stringBuffer.append("温度：" + todayWeather.get("temperature").toString() + "\n");
                    stringBuffer.append("天气：" + todayWeather.get("weather").toString() + "\n");
                    stringBuffer.append("风向：" + todayWeather.get("wind").toString() + "\n");
                    stringBuffer.append("日期：" + todayWeather.get("date_y").toString() + "\n");
                    stringBuffer.append("穿衣指数：" + todayWeather.get("dressing_index").toString() + "\n");
                    stringBuffer.append("穿衣建议：" + todayWeather.get("dressing_advice").toString() + "\n");
                    stringBuffer.append("紫外线强度：" + todayWeather.get("uv_index").toString() + "\n");
                    stringBuffer.append("干燥指数：" + todayWeather.get("drying_index").toString());
                    result = stringBuffer.toString();
                } else {
                    result = "由于不可抗力因素，喵帕斯暂时查不到天气了";
                }
            } else {
                if (result != null && result.equals("202")) {
                    result = "您说的这个城市……它是二次元的城市吗？";
                } else {
                    result = "由于不可抗力因素，喵帕斯暂时查不到天气了";
                }
            }
        }
        System.out.println(result);
        return result;
    }

    public static String getTopNews() {
        String result = excuteTopNews();//返回接口结果,得到json格式数据
        if (result != null) {
            JSONObject obj = JSONObject.parseObject(result);
            JSONArray jsonArray = JSONArray.parseArray(obj.getJSONObject("result").get("data").toString());
            List<TopNews> topNews = jsonArray.toJavaList(TopNews.class);
            if (topNews != null && !topNews.isEmpty()) {//200表示成功返回数据
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("喵帕斯为您播报今日头条：\n\n");
                topNews.forEach(t -> {
                    stringBuffer.append(t.getTitle() + "\n");
                    stringBuffer.append(t.getUrl() + "\n\n");
                });
                result = stringBuffer.toString();
            } else {
                result = "由于不可抗力因素，喵帕斯暂时查不到新闻了";
            }
        }
        return result;
    }
}