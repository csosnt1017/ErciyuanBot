package com.example.dao.mapper;

import com.example.config.druid.DataSourceConfig;
import com.example.dao.entity.StatementWarehouseImg;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Copyright © 2019年 erciyuanboot. All rights reserved.
 *
 * @author 古今
 * <p>
 * xxxxx类
 * @date 2019/09/12
 * <p>
 * Modification History:
 * Date     Author    Version      Description
 * ---------------------------------------------------------*
 * 2019/09/12   古今   v1.0.0       新增
 */
public class StatementWarehouseImgMapper {
    public int insert(StatementWarehouseImg statementWarehouseImg){
        statementWarehouseImg.setGmtCreate(LocalDateTime.now());
        return DataSourceConfig.getSession().insert("com.example.dao.mapper.StatementWarehouseImgMapper.insert", statementWarehouseImg);
    }

    public int update(StatementWarehouseImg statementWarehouseImg){
        return DataSourceConfig.getSession().update("com.example.dao.mapper.StatementWarehouseImgMapper.update", statementWarehouseImg);
    }

    public StatementWarehouseImg selectById(Long id){
        return DataSourceConfig.getSession().selectOne("com.example.dao.mapper.StatementWarehouseImgMapper.selectById",id);
    }

    public List<StatementWarehouseImg> selectByFileKey(String fileKey){
        return DataSourceConfig.getSession().selectList("selectByFileKey",fileKey);
    }

    public List<StatementWarehouseImg> selectAll(){
        return DataSourceConfig.getSession().selectList("com.example.dao.mapper.StatementWarehouseImgMapper.selectAll");
    }
}