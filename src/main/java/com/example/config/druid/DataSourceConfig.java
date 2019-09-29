package com.example.config.druid;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;

import javax.sql.DataSource;

/**
 * 啊里巴巴druid数据连接池配置
 *
 * @author gj
 * @date 2018/12/19
 */
public class DataSourceConfig {
    private static volatile SqlSession sqlSession;

    private DataSourceConfig(){}

    public static SqlSession getSession() {
        if (sqlSession == null) {
            synchronized (DataSourceConfig.class) {
                if (sqlSession == null) {
                    String resources = "mybatis-config.xml";
                    //创建流
                    Reader reader = null;
                    try {
                        //读取mybatis-config.xml文件到reader对象中
                        reader = Resources.getResourceAsReader(resources);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //初始化mybatis,创建SqlSessionFactory类的实例
                    SqlSessionFactory sqlMapper = new SqlSessionFactoryBuilder().build(reader);
                    //创建session实例
                    sqlSession = sqlMapper.openSession(true);
                }
            }
        }
        return sqlSession;
    }
}
