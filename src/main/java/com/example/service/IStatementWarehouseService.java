package com.example.service;

import com.example.dao.entity.StatementWarehouse;

import java.util.List;

/**
 * Copyright © 2019年 erciyuanboot. All rights reserved.
 *
 * @author 古今
 * <p>
 * 语句仓库业务接口
 * @date 2019/09/23
 * <p>
 * Modification History:
 * Date     Author    Version      Description
 * ---------------------------------------------------------*
 * 2019/09/23   古今   v1.0.0       新增
 */
public interface IStatementWarehouseService {
    /**
     * @Description: 创建语句
     * @return: String
     * @Author: gj
     * @Date: 2019/9/23
     */
    String createStatementWarehouse(StatementWarehouse statementWarehouse);

    /**
     * @Description: 修改语句
     * @return: String
     * @Author: gj
     * @Date: 2019/9/23
     */
    String updateStatementWarehouse(StatementWarehouse statementWarehouse);

    /**
     * @Description: 获取语句列表
     * @return: List<StatementWarehouse>
     * @Author: gj
     * @Date: 2019/9/23
     */
    List<StatementWarehouse> listWarehouse(String keyWord);
    /**
    * @Description: 初始化语句
    * @return: void
    * @Author: gj
    * @Date: 2019/9/23
    */
    List<StatementWarehouse> initStatementWarehouse();
}