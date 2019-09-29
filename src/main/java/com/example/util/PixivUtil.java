package com.example.util;

import com.example.image.DownLoadPic;
import com.example.model.PixivImgRankDO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class PixivUtil {

    private static final String REGEXP_PAGE = "^[1-9]{1}$";

    private static final String REGEXP_WORKID = "^[0-9]{8}$";

    /**
     * @Description: 获取p站图片入口
     * @return:
     * @Author: gj
     * @Date: 2019/9/26
     */
    public static List<String> listImg(String msg) {
        List<String> returnMsgList = new ArrayList<>();
        if (msg.contains("大图")) {
            returnMsgList.add(listImgDetail(msg));
        } else if (msg.contains("排行")) {
            returnMsgList = listRankImg(msg);
        }
        return returnMsgList;
    }

    /**
     * @Description: 获取插画大图
     * @return:
     * @Author: gj
     * @Date: 2019/9/26
     */
    public static String listImgDetail(String msg) {
        StringBuffer stringBuffer = new StringBuffer();
        String returnMsg = null;
        try {
            String imageUrl = DownLoadPic.getImageUrl(StatementWarehouseUtil.getCookie(), DownLoadPic.getDetailUrl(null, getWorkId(msg)));
            String imageName = DownLoadPic.download(imageUrl, DownLoadPic.getDetailUrl(null, getWorkId(msg)));
            returnMsg = formatImgName(imageName);
        } catch (RuntimeException e) {
            return e.getMessage();
        }
        stringBuffer.append(returnMsg);
        return returnMsg;
    }

    /**
     * @Description: 获取每日排行图片
     * @return:
     * @Author: gj
     * @Date: 2019/9/25
     */
    public static List<String> listRankImg(String msg) {
        StringBuffer stringBuffer = new StringBuffer();
        List<String> imgList = new ArrayList<>();
        try {
            List<PixivImgRankDO> imgRankDOList = DownLoadPic.listImageUrl(StatementWarehouseUtil.getCookie(),
                    DownLoadPic.getRankUrl(getPage(msg), getMode(msg), getContent(msg)));
            stringBuffer.append(LocalDate.now().toString() + " Pixiv 排行" + getCount(getPage(msg)) + "\n\n");
            int i = 0;
            for (PixivImgRankDO img : imgRankDOList) {
                String imgName = DownLoadPic.download(img.getUrl(), DownLoadPic.getDetailUrl(null, img.getIllustId()));
                stringBuffer.append("作品名：" + img.getTitle() + "\n");
                stringBuffer.append("画师：" + img.getUserName() + "\n");
                stringBuffer.append(formatImgName(imgName) + "\n\n");
                if (i == 16) {
                    imgList.add(stringBuffer.toString());
                    stringBuffer.setLength(0);
                } else if (i == 33) {
                    imgList.add(stringBuffer.toString());
                    stringBuffer.setLength(0);
                } else if (i == 49) {
                    imgList.add(stringBuffer.toString());
                }
                i++;
            }
            imgList.add("图片装载完毕，若要查看其他页，直接在排行后加页数就可以了，如p站排行2\n" +
                    "输入#p站搜索查看搜索功能");
        } catch (RuntimeException e) {
            imgList.add(e.getMessage());

        }
        return imgList;
    }

    /**
     * @Description: 格式化P站图片名
     * @return:
     * @Author: gj
     * @Date: 2019/9/25
     */
    public static String formatImgName(String imgName) {
        return "[CQ:image,file=" + imgName + "]";
    }


    public static String getCount(String page) {
        String count = null;
        switch (page) {
            case "1":
                count = "1~50位";
                break;
            case "2":
                count = "51~100位";
                break;
            case "3":
                count = "101~150位";
                break;
            case "4":
                count = "151~200位";
                break;
            case "5":
                count = "201~250位";
                break;
            case "6":
                count = "251~300位";
                break;
            case "7":
                count = "301~350位";
                break;
            case "8":
                count = "351~400位";
                break;
            case "9":
                count = "401~450位";
                break;
            default:
                count = "451~500位";
        }
        return count;
    }

    /**
     * @Description: 校验是否p站图片请求
     * @return:
     * @Author: gj
     * @Date: 2019/9/25
     */
    public static boolean checkPixiv(String msg) {
        if (msg.contains("p站")) {
            int index = msg.indexOf("排行");
            int index1 = msg.indexOf("大图");
            if (index != -1 || index1 != -1) {
                return true;
            }
        }
        return false;
    }

    /**
     * @Description: 校验页数
     * @return:
     * @Author: gj
     * @Date: 2019/9/25
     */
    public static boolean checkPage(String page) {
        Pattern p = Pattern.compile(REGEXP_PAGE);
        Matcher m = p.matcher(page);
        return m.find();
    }

    /**
     * @Description: 校验作品id
     * @return:
     * @Author: gj
     * @Date: 2019/9/25
     */
    public static boolean checkWorkId(String workId) {
        Pattern p = Pattern.compile(REGEXP_WORKID);
        Matcher m = p.matcher(workId);
        return m.find();
    }


    /**
     * @Description: 获取页数
     * @return:
     * @Author: gj
     * @Date: 2019/9/25
     */
    public static String getPage(String msg) {
        int index = msg.indexOf("排行");
        String page = msg.substring(index + 2);
        if (!checkPage(page)) {
            throw new RuntimeException("请输入正确的页数，范围1~9");
        }
        if (msg.contains("r18动图")) {
            if (Integer.parseInt(page) > 1) {
                throw new RuntimeException("r18动图只有1页");
            }
        }
        return page;
    }

    /**
     * @Description: 获取作品id
     * @return:
     * @Author: gj
     * @Date: 2019/9/25
     */
    public static String getWorkId(String msg) {
        int index = msg.indexOf(" ");
        if (index == -1) {
            throw new RuntimeException("搜索格式错误，正确格式：p站大图+空格+作品id");
        }
        String workId = msg.substring(index + 1);
        if (!checkWorkId(workId)) {
            throw new RuntimeException("请输入正确的作品id");
        }
        return workId;
    }

    /**
     * @Description: 获取模式
     * @return:
     * @Author: gj
     * @Date: 2019/9/25
     */
    public static String getMode(String msg) {
        return PixivRankUtil.getMode(msg);
    }

    /**
     * @Description: 获取内容
     * @return:
     * @Author: gj
     * @Date: 2019/9/25
     */
    public static String getContent(String msg) {
        return PixivRankUtil.getConent(msg);
    }


    public static String getSearchTip() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("欢迎使用喵帕斯pixiv图片搜索功能！\n\n");
        stringBuffer.append("目前有两个搜索功能：\n\n");
        stringBuffer.append("1.搜索图片排行，可按筛选条件和图片分类进行搜索\n");
        stringBuffer.append("图片分类：综合、插画、动图、漫画\n");
        stringBuffer.append("筛选条件：今日、本周、本月、原创、受男性欢迎、受女性欢迎、r18\n");
        stringBuffer.append("格式：p站+筛选条件+图片分类+排行+页数，如搜索今日的插画排行第一页需要发送：p站今日插画排行1\n\n");
        stringBuffer.append("2.搜索大图\n");
        stringBuffer.append("格式：p站大图+空格+作品id，如p站大图 76943237");
        return stringBuffer.toString();
    }
}