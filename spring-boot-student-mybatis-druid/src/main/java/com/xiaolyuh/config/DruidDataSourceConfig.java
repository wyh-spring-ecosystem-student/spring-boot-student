package com.xiaolyuh.config;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;

@Configuration
@ConditionalOnClass(com.alibaba.druid.pool.DruidDataSource.class)
@ConditionalOnProperty(name = "spring.datasource.type", havingValue = "com.alibaba.druid.pool.DruidDataSource", matchIfMissing = true)
@ServletComponentScan("com.xiaolyuh.filters")
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

        datasource.setInitialSize(druidDataSourceProperty.getInitialSize());
        datasource.setMinIdle(druidDataSourceProperty.getMinIdle());
        datasource.setMaxActive(druidDataSourceProperty.getMaxActive());
        // 配置获取连接等待超时的时间
        datasource.setMaxWait(druidDataSourceProperty.getMaxWait());
        // 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
        datasource.setTimeBetweenEvictionRunsMillis(druidDataSourceProperty.getTimeBetweenEvictionRunsMillis());
        // 配置一个连接在池中最小生存的时间，单位是毫秒
        datasource.setMinEvictableIdleTimeMillis(druidDataSourceProperty.getMinEvictableIdleTimeMillis());
        datasource.setValidationQuery(druidDataSourceProperty.getValidationQuery());
        datasource.setTestWhileIdle(druidDataSourceProperty.isTestWhileIdle());
        datasource.setTestOnBorrow(druidDataSourceProperty.isTestOnBorrow());
        datasource.setTestOnReturn(druidDataSourceProperty.isTestOnReturn());
        datasource.setPoolPreparedStatements(druidDataSourceProperty.isPoolPreparedStatements());
        datasource.setMaxPoolPreparedStatementPerConnectionSize(druidDataSourceProperty.getMaxPoolPreparedStatementPerConnectionSize());
        datasource.setUseGlobalDataSourceStat(druidDataSourceProperty.isUseGlobalDataSourceStat());
        try {
            datasource.setFilters(druidDataSourceProperty.getFilters());
        } catch (SQLException e) {
            logger.error("druid configuration initialization filter", e);
        }
        datasource.setConnectionProperties(druidDataSourceProperty.getConnectionProperties());

        return datasource;
    }
    
//    // 配置Druid监控的StatViewServlet和WebStatFilter
//    @Bean
//    public ServletRegistrationBean druidServlet() {
//    	logger.info("init Druid Servlet Configuration ");
//      ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean();
//      servletRegistrationBean.setServlet(new StatViewServlet());
//      servletRegistrationBean.addUrlMappings("/druid/*");
//      Map<String, String> initParameters = new HashMap<String, String>();
//      initParameters.put("loginUsername", "admin");// 用户名
//      initParameters.put("loginPassword", "admin");// 密码
//      initParameters.put("resetEnable", "false");// 禁用HTML页面上的“Reset All”功能
//      initParameters.put("allow", ""); // IP白名单 (没有配置或者为空，则允许所有访问)
//      //initParameters.put("deny", "192.168.20.38");// IP黑名单 (存在共同时，deny优先于allow)
//      servletRegistrationBean.setInitParameters(initParameters);
//      return servletRegistrationBean;
//    }
//
//    @Bean
//    public FilterRegistrationBean filterRegistrationBean() {
//      FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
//      filterRegistrationBean.setFilter(new WebStatFilter());
//      filterRegistrationBean.addUrlPatterns("/*");
//      filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
//      return filterRegistrationBean;
//    }
}  