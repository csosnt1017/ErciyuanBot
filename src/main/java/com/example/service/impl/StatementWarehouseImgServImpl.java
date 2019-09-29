package com.example.service.impl;

import com.example.dao.entity.StatementWarehouseImg;
import com.example.dao.mapper.StatementWarehouseImgMapper;
import com.example.service.IStatementWarehouseImgService;

import java.util.List;

/**
 * Copyright © 2019年 erciyuanboot. All rights reserved.
 *
 * @author 古今
 * <p>
 * 语句图片业务类
 * @date 2019/09/23
 * <p>
 * Modification History:
 * Date     Author    Version      Description
 * ---------------------------------------------------------*
 * 2019/09/23   古今   v1.0.0       新增
 */
public class StatementWarehouseImgServImpl implements IStatementWarehouseImgService {
    private final static StatementWarehouseImgMapper statementWarehouseImgMapper = new StatementWarehouseImgMapper();
    private static List<StatementWarehouseImg> statementWarehouseImgList;
    @Override
    public String createStatementWarehouseImg(StatementWarehouseImg statementWarehouseImg) {
        String returnMsg = null;
        int insert = 0;
        try {
            insert = statementWarehouseImgMapper.insert(statementWarehouseImg);
        } catch (Exception e) {
            if (e.getMessage().contains("UK_JCQ_STATEMENT_WAREHOUSE_IMG_1")) {
                return "库里已经存在相同的数据啦！";
            }
        }
        if (insert == 0) {
            returnMsg = "喵帕斯的插入功能暂不可用，请联系master!";
        } else {
            returnMsg = "主人的请求已成功添加到库里啦！现在去试试吧！";
            initStatementWarehouseImg();
        }
        return returnMsg;
    }

    @Override
    public List<StatementWarehouseImg> initStatementWarehouseImg() {
        statementWarehouseImgList = statementWarehouseImgMapper.selectAll();
        return statementWarehouseImgList;
    }

    public static List<StatementWarehouseImg> getStatementWarehouseImgList() {
        return statementWarehouseImgList;
    }
}