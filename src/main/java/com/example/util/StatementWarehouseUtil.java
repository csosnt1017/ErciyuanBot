package com.example.util;

import com.example.dao.entity.StatementWarehouse;
import com.example.dao.entity.StatementWarehouseImg;
import com.example.dao.mapper.StatementWarehouseImgMapper;
import com.example.dao.mapper.StatementWarehouseMapper;
import com.example.service.IStatementWarehouseImgService;
import com.example.service.IStatementWarehouseService;
import com.example.service.impl.StatementWarehouseImgServImpl;
import com.example.service.impl.StatementWarehouseServImpl;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.sobte.cqp.jcq.entity.CQImage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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
public class StatementWarehouseUtil {
    private final static String secretKey = "gkd";
    private final static String plusSecretKey = "gkdplus";
    private final static String banSecretKey = "gkdban";
    private final static String banKeyWord = "ban";
    private final static IStatementWarehouseImgService statementWarehouseImgService = new StatementWarehouseImgServImpl();
    private final static IStatementWarehouseService statementWarehouseService = new StatementWarehouseServImpl();
    private static List<StatementWarehouseImg> statementWarehouseImgList;
    private static List<StatementWarehouse> statementWarehouseList;
    private static List<StatementWarehouse> banList;


    public static String insertWarehouse(String msg, long fromQQ) {
        String returnMsg = null;
        if (msg.length() > 6) {
            if (msg.substring(0, 3).equals(secretKey)) {
                if (msg.contains("1") && msg.contains("2")) {
                    if (msg.contains("天气") || msg.contains("头条") || msg.contains("功能") || msg.contains("1ban") || msg.contains("p站搜索")) {
                        return "库里已经存在相同的数据啦！";
                    }
                    if (msg.contains("[CQ:image")) {
                        returnMsg = insertImg(msg, fromQQ);
                    } else {
                        returnMsg = insertTxt(msg, fromQQ);
                    }
                }
            }
        }
        return returnMsg;
    }

    public static String insertTxt(String msg, long fromQQ) {
        int start = msg.indexOf("1");
        int end = msg.indexOf("2");
        String keyWord = msg.substring(start + 1, end);
        String keyValue = msg.substring(end + 1);
        StatementWarehouse statementWarehouse = new StatementWarehouse();
        statementWarehouse.setKeyWord(keyWord);
        statementWarehouse.setKeyValue(keyValue);
        statementWarehouse.setFromQQ(String.valueOf(fromQQ));
        if (msg.contains(banSecretKey)) {
            statementWarehouse.setKeyWord(banKeyWord);
            msg = statementWarehouseService.createStatementWarehouse(statementWarehouse);
        } else if (!msg.contains(plusSecretKey)) {
            List<StatementWarehouse> statementWarehouseList = statementWarehouseService.listWarehouse(keyWord);
            if (statementWarehouseList != null && statementWarehouseList.size() == 1) {
                statementWarehouse.setId(statementWarehouseList.get(0).getId());
                msg = statementWarehouseService.updateStatementWarehouse(statementWarehouse);
            } else if (statementWarehouseList != null && statementWarehouseList.size() > 1) {
                return "库里已经有很多条你想加的关键词啦，请使用gkdplus命令来添加！";
            } else {
                msg = statementWarehouseService.createStatementWarehouse(statementWarehouse);
            }
        } else {
            msg = statementWarehouseService.createStatementWarehouse(statementWarehouse);
        }
        statementWarehouseList = StatementWarehouseServImpl.getStatementWarehouseList();
        initBan();
        return msg;
    }

    public static String insertImg(String msg, long fromQQ) {
        int start = msg.indexOf("1");
        int end = msg.indexOf("]2[");
        int end2 = msg.indexOf("]2");
        int end3 = msg.indexOf("2[");
        String fileKey = null;
        String fileName = null;
        String returnMsg = null;

        if (end != -1) {
            fileKey = msg.substring(start + 1, end + 1);
            fileName = msg.substring(end + 2);
            returnMsg = insertImg(fileKey, fileName, fromQQ);
        } else if (end2 != -1) {
            fileKey = msg.substring(start + 1, end2 + 1);
            fileName = msg.substring(end2 + 2);
            returnMsg = insertImg(fileKey, fileName, fromQQ);
        } else {
            fileKey = msg.substring(start + 1, end3);
            String fileNames = msg.substring(end3 + 1);
            String[] splitFileName = fileNames.split("]");
            for (String fileNameNew : splitFileName) {
                returnMsg = insertImg(fileKey, fileNameNew + "]", fromQQ);
            }
        }
        statementWarehouseImgList = StatementWarehouseImgServImpl.getStatementWarehouseImgList();
        return returnMsg;
    }

    public static String insertImg(String key, String value, long fromQQ) {
        String returnMsg = null;
        StatementWarehouseImg statementWarehouseImg = new StatementWarehouseImg();
        statementWarehouseImg.setFileKey(key);
        statementWarehouseImg.setFileName(value);
        statementWarehouseImg.setFromQQ(String.valueOf(fromQQ));
        returnMsg = statementWarehouseImgService.createStatementWarehouseImg(statementWarehouseImg);
        return returnMsg;
    }

    public static void initAll() {
        statementWarehouseList = statementWarehouseService.initStatementWarehouse();
        statementWarehouseImgList = statementWarehouseImgService.initStatementWarehouseImg();
        initBan();
    }

    public static void initBan() {
        banList = statementWarehouseList.stream().filter(statementWarehouse -> banKeyWord.equals(statementWarehouse.getKeyWord())).collect(Collectors.toList());
    }

    public static String selectWarehouse(String msg) {
        String returnMsg = null;
        if (msg.contains("#") && !msg.contains("[CQ") && !msg.contains("&#")) {
            int start = msg.indexOf("#");
            msg = msg.substring(start + 1);
            returnMsg = selectWarehouseByImg(msg);
            if (returnMsg == null) {
                returnMsg = selectWarehouseByTxt(msg);
                if (returnMsg == null) {
                    returnMsg = "喵帕斯找不到你说的内容了喵，请先把内容加到我里面来！";
                }
            }
        } else if (msg.contains("五五开") || msg.contains("卢本伟")) {
            returnMsg = selectWarehouseByImg("卢本伟");
        } else if (msg.contains("土块")) {
            returnMsg = selectWarehouseByImg("土块");
        }
        return returnMsg;
    }

    public static String selectWarehouseByTxt(String msg) {
        String reutnMsg = null;
        if (msg.contains("功能")) {
            reutnMsg = getFunction();
        } else if (msg.contains("天气")) {
            reutnMsg = ThirdPartyUtil.getWeather(msg);
        } else if (msg.equals("p站搜索")) {
            reutnMsg = PixivUtil.getSearchTip();
        } else {
            if (statementWarehouseList != null) {
                List<StatementWarehouse> statementWarehouseListNew = StatementWarehouseUtil.statementWarehouseList.stream().filter(statementWarehouse -> msg.equals(statementWarehouse.getKeyWord())).collect(Collectors.toList());
                if (!statementWarehouseListNew.isEmpty()) {
                    int index = (int) (Math.random() * statementWarehouseListNew.size());
                    reutnMsg = statementWarehouseListNew.get(index).getKeyValue();
                }
            }
        }
        return reutnMsg;
    }

    public static String selectWarehouseByImg(String file) {
        String reutnMsg = null;
        if (statementWarehouseImgList != null) {
            List<StatementWarehouseImg> statementWarehouseImgListNew = StatementWarehouseUtil.statementWarehouseImgList.stream().filter(statementWarehouseImg -> file.equals(statementWarehouseImg.getFileKey())).collect(Collectors.toList());
            if (!statementWarehouseImgListNew.isEmpty()) {
                int index = (int) (Math.random() * statementWarehouseImgListNew.size());
                reutnMsg = statementWarehouseImgListNew.get(index).getFileName();
            }
        }
        return reutnMsg;
    }

    public static String selectBan(String msg) {
        String reutnMsg = null;
        if (banList != null && !banList.isEmpty()) {
            if (banList.stream().anyMatch(statementWarehouse -> msg.contains(statementWarehouse.getKeyValue()))) {
                reutnMsg = "警告！你的发言含有敏感词汇，请不要再发了！多次警告无效将作踢出群处理！";
            }
        }
        return reutnMsg;
    }

    public static String getCookie() {
        StatementWarehouse cookieStatement = statementWarehouseList.stream().filter(statementWarehouse -> "cookie"
                .equals(statementWarehouse.getKeyWord())).findFirst().get();
        return cookieStatement.getKeyValue();
    }

    public static String getFunction() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("欢迎使用喵帕斯助手，目前拥有的功能如下：\n\n");
        stringBuffer.append("1.关键词回复：#+关键字，如#二次元语录，助手将从库里读取关键字回复内容（回复内容可以是文字和图片），如果库里有多条相同关键字的数据，那么将随机选择一条回复\n\n");
        stringBuffer.append("2.图片回复：发送指定图片，助手将从库里读取图片回复内容（回复内容可以是文字和图片）\n\n");
        stringBuffer.append("3.查询天气：#地点+天气，如#南昌天气，助手将直接从网上查询天气\n\n");

        stringBuffer.append("★关键词与图片回复需先把关键词与回复内容加进助手里面，格式：gkd1关键词（图片）2回复内容（图片或文字），如gkd1图片2图片图片");
        return stringBuffer.toString();
    }
}