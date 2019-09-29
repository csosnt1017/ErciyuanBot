package com.example.service.impl;

import com.example.dao.entity.StatementWarehouse;
import com.example.dao.mapper.StatementWarehouseMapper;
import com.example.service.IStatementWarehouseService;
import com.example.util.StatementWarehouseUtil;

import java.util.List;

/**
 * Copyright © 2019年 erciyuanboot. All rights reserved.
 *
 * @author 古今
 * <p>
 * 语句仓库业务类
 * @date 2019/09/23
 * <p>
 * Modification History:
 * Date     Author    Version      Description
 * ---------------------------------------------------------*
 * 2019/09/23   古今   v1.0.0       新增
 */
public class StatementWarehouseServImpl implements IStatementWarehouseService {
    private final static StatementWarehouseMapper statementWarehouseMapper = new StatementWarehouseMapper();
    private static List<StatementWarehouse> statementWarehouseList;

    @Override
    public String createStatementWarehouse(StatementWarehouse statementWarehouse) {
        String returnMsg = null;
        int insert = 0;
        try {
            insert = statementWarehouseMapper.insert(statementWarehouse);
        } catch (Exception e) {
            if (e.getMessage().contains("UK_JCQ_STATEMENT_WAREHOUSE_1")) {
                return "库里已经存在相同的数据啦！";
            }
        }
        if (insert == 0) {
            returnMsg = "喵帕斯的插入功能暂不可用，请联系master!";
        } else {
            returnMsg = "主人的请求已成功添加到库里啦！现在去试试吧！";
            statementWarehouseList = statementWarehouseMapper.selectAll();
        }
        return returnMsg;
    }

    @Override
    public String updateStatementWarehouse(StatementWarehouse statementWarehouse) {
        String returnMsg = null;
        int update = statementWarehouseMapper.update(statementWarehouse);
        if (update == 0) {
            returnMsg = "喵帕斯的插入功能暂不可用，请联系master!";
        } else {
            returnMsg = "主人的请求已成功添加到库里啦！现在去试试吧！";
            initStatementWarehouse();
        }
        return returnMsg;
    }

    @Override
    public List<StatementWarehouse> listWarehouse(String keyWord) {
        return statementWarehouseMapper.selectByKeyWord(keyWord);
    }

    @Override
    public List<StatementWarehouse> initStatementWarehouse() {
        statementWarehouseList = statementWarehouseMapper.selectAll();
        return statementWarehouseList;
    }

    public static List<StatementWarehouse> getStatementWarehouseList() {
        return statementWarehouseList;
    }

}