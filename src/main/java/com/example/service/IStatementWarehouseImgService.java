package com.example.service;

import com.example.dao.entity.StatementWarehouseImg;

import java.util.List;

/**
 * Copyright © 2019年 erciyuanboot. All rights reserved.
 *
 * @author 古今
 * <p>
 * 语句图片业务接口
 * @date 2019/09/23
 * <p>
 * Modification History:
 * Date     Author    Version      Description
 * ---------------------------------------------------------*
 * 2019/09/23   古今   v1.0.0       新增
 */
public interface IStatementWarehouseImgService {
    /** 
    * @Description: 创建图片语句
    * @return: String
    * @Author: gj 
    * @Date: 2019/9/23 
    */
    String createStatementWarehouseImg(StatementWarehouseImg statementWarehouseImg);
    /**
    * @Description: 初始化图片语句
    * @return: void
    * @Author: gj
    * @Date: 2019/9/23
    */
    List<StatementWarehouseImg> initStatementWarehouseImg();
}