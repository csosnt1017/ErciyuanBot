package com.example.dao.mapper;

import com.example.config.druid.DataSourceConfig;
import com.example.dao.entity.StatementWarehouse;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Copyright © 2019年 erciyuanboot. All rights reserved.
 *
 * @author 古今
 * <p>
 * xxxxx类
 * @date 2019/09/10
 * <p>
 * Modification History:
 * Date     Author    Version      Description
 * ---------------------------------------------------------*
 * 2019/09/10   古今   v1.0.0       新增
 */
public class StatementWarehouseMapper {
    public int insert(StatementWarehouse statementWarehouse){
        statementWarehouse.setGmtCreate(LocalDateTime.now());
        return  DataSourceConfig.getSession().insert("com.example.dao.mapper.StatementWarehouseMapper.insert", statementWarehouse);
    }

    public int update(StatementWarehouse statementWarehouse){
        return DataSourceConfig.getSession().update("com.example.dao.mapper.StatementWarehouseMapper.update", statementWarehouse);
    }

    public StatementWarehouse selectById(Long id){
        return DataSourceConfig.getSession().selectOne("com.example.dao.mapper.StatementWarehouseMapper.selectById",id);
    }

    public List<StatementWarehouse> selectByKeyWord(String keyWord){
        return DataSourceConfig.getSession().selectList("selectByKeyWord",keyWord);
    }

    public List<StatementWarehouse> selectAll(){
        return DataSourceConfig.getSession().selectList("com.example.dao.mapper.StatementWarehouseMapper.selectAll");
    }
}