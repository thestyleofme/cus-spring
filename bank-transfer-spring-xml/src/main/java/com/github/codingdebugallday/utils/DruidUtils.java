package com.github.codingdebugallday.utils;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * <p>
 * description
 * </p>
 * 
 * @author isaac 2020/9/5 2:45
 * @since 1.0.0
 */
public class DruidUtils {

    private DruidUtils(){
    }

    private static final DruidDataSource DRUID_DATASOURCE = new DruidDataSource();

    static {
        DRUID_DATASOURCE.setDriverClassName("com.mysql.jdbc.Driver");
        DRUID_DATASOURCE.setUrl("jdbc:mysql://localhost:3306/cus_spring?useUnicode=true&characterEncoding=utf-8&useSSL=false");
        DRUID_DATASOURCE.setUsername("root");
        DRUID_DATASOURCE.setPassword("tse@9527");
    }

    public static DruidDataSource getInstance() {
        return DRUID_DATASOURCE;
    }

}
