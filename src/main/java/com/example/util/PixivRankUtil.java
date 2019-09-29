package com.example.util;

import com.example.constant.PixivConst;

/**
 * Copyright © 2019年 erciyuanboot. All rights reserved.
 *
 * @author 古今
 * <p>
 * xxxxx类
 * @date 2019/09/26
 * <p>
 * Modification History:
 * Date     Author    Version      Description
 * ---------------------------------------------------------*
 * 2019/09/26   古今   v1.0.0       新增
 */
public class PixivRankUtil {

    public static String getMode(String msg) {
        String mode = null;
        if (msg.contains(PixivConst.RankMode.gruop_1)) {
            mode = PixivConst.Mode.MODE_DAILY;
        } else if (msg.contains(PixivConst.RankMode.gruop_2)) {
            mode = PixivConst.Mode.MODE_DAILY;
        } else if (msg.contains(PixivConst.RankMode.gruop_3)) {
            mode = PixivConst.Mode.MODE_WEEKLY;
        } else if (msg.contains(PixivConst.RankMode.gruop_4)) {
            mode = PixivConst.Mode.MODE_MONTHLY;
        } else if (msg.contains(PixivConst.RankMode.gruop_5)) {
            mode = PixivConst.Mode.MODE_ORIGINAL;
        } else if (msg.contains(PixivConst.RankMode.gruop_6)) {
            mode = PixivConst.Mode.MODE_MALE;
        } else if (msg.contains(PixivConst.RankMode.gruop_7)) {
            mode = PixivConst.Mode.MODE_FEMALE;
        } else if (msg.contains(PixivConst.RankMode.gruop_8)) {
            mode = PixivConst.Mode.MODE_R18;
        } else {
            throw new RuntimeException("请输入正确的筛选条件！");
        }
        return mode;
    }

    public static String getConent(String msg) {
        String content = null;
        if (msg.contains(PixivConst.RankContent.gruop_1) || msg.contains(PixivConst.RankContent.gruop_2)) {
        } else if (msg.contains(PixivConst.RankContent.gruop_3)) {
            content = PixivConst.Content.CONTENT_ILLUST;
        } else if (msg.contains(PixivConst.RankContent.gruop_4)) {
            content = PixivConst.Content.CONTENT_UGOIRA;
        } else if (msg.contains(PixivConst.RankContent.gruop_5)) {
            content = PixivConst.Content.CONTENT_MANGA;
        } else {
            throw new RuntimeException("请输入正确的图片分类！");
        }
        return content;
    }
}