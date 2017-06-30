package com.xiaolyuh.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
@ConditionalOnClass(com.alibaba.druid.pool.DruidDataSource.class)
@ConditionalOnProperty(name = "spring.datasource.type", havingValue = "com.alibaba.druid.pool.DruidDataSource", matchIfMissing = true)
public class DruidDataSourceConfig {

    private Logger logger = LoggerFactory.getLogger(DruidDataSourceConfig.class);

    @Autowired
    private DruidDataSourceProperty druidDataSourceProperty;


    @Bean     //声明其为Bean实例
    @Primary  //在同样的DataSource中，首先使用被标注的DataSource
    public DataSource dataSource(){
        DruidDataSource datasource = new DruidDataSource();

        datasource.setUrl(druidDataSourceProperty.getUrl());
        datasource.setUsername(druidDataSourceProperty.getUsername());
        datasource.setPassword(druidDataSourceProperty.getPassword());
        datasource.setDriverClassName(druidDataSourceProperty.getDriverClassName());

        //configuration
        datasource.setInitialSize(druidDataSourceProperty.getInitialSize());
        datasource.setMinIdle(druidDataSourceProperty.getMinIdle());
        datasource.setMaxActive(druidDataSourceProperty.getMaxActive());
        datasource.setMaxWait(druidDataSourceProperty.getMaxWait());
        datasource.setTimeBetweenEvictionRunsMillis(druidDataSourceProperty.getTimeBetweenEvictionRunsMillis());
        datasource.setMinEvictableIdleTimeMillis(druidDataSourceProperty.getMinEvictableIdleTimeMillis());
        datasource.setValidationQuery(druidDataSourceProperty.getValidationQuery());
        datasource.setTestWhileIdle(druidDataSourceProperty.isTestWhileIdle());
        datasource.setTestOnBorrow(druidDataSourceProperty.isTestOnBorrow());
        datasource.setTestOnReturn(druidDataSourceProperty.isTestOnReturn());
        datasource.setPoolPreparedStatements(druidDataSourceProperty.isPoolPreparedStatements());
        datasource.setMaxPoolPreparedStatementPerConnectionSize(druidDataSourceProperty.getMaxPoolPreparedStatementPerConnectionSize());
        try {
            datasource.setFilters(druidDataSourceProperty.getFilters());
        } catch (SQLException e) {
            logger.error("druid configuration initialization filter", e);
        }
        datasource.setConnectionProperties(druidDataSourceProperty.getConnectionProperties());

        return datasource;
    }
}  