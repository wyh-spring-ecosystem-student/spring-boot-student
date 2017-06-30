package com.xiaolyuh.filters;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

import com.alibaba.druid.support.http.WebStatFilter;

//最后在App.Java类上加上注解：@ServletComponentScan是的spring能够扫描到我们自己编写的servlet和filter。
//
//注意不要忘记在 SpringBootSampleApplication.java 上添加 @ServletComponentScan 注解，不然就是404了。
//
//然后启动项目后访问 http://127.0.0.1:8080/druid/index.html 即可查看数据源及SQL统计等。 
@WebFilter(filterName = "druidWebStatFilter", urlPatterns = "/*",
        initParams = {
                @WebInitParam(name = "exclusions", value = "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*")//忽略资源
        }
)
public class DruidStatFilter extends WebStatFilter {

} 