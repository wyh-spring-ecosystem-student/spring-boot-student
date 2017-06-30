package com.xiaolyuh.filters;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

import com.alibaba.druid.support.http.StatViewServlet;

//最后在App.Java类上加上注解：@ServletComponentScan是的spring能够扫描到我们自己编写的servlet和filter。
//
//注意不要忘记在 SpringBootSampleApplication.java 上添加 @ServletComponentScan 注解，不然就是404了。
//
//然后启动项目后访问 http://127.0.0.1:8080/druid/index.html 即可查看数据源及SQL统计等。 
@WebServlet(urlPatterns = "/druid/*",
        initParams = {
                @WebInitParam(name = "allow", value = "127.0.0.1,192.168.163.1"),// IP白名单(没有配置或者为空，则允许所有访问)
                @WebInitParam(name = "deny", value = "192.168.1.73"),// IP黑名单 (存在共同时，deny优先于allow)
                @WebInitParam(name = "loginUsername", value = "admin"),// 用户名
                @WebInitParam(name = "loginPassword", value = "123456"),// 密码
                @WebInitParam(name = "resetEnable", value = "false")// 禁用HTML页面上的“Reset All”功能
        })
public class DruidStatViewServlet extends StatViewServlet {
    private static final long serialVersionUID = -2688872071445249539L;

}