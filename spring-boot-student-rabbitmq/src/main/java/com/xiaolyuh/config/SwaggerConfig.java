package com.xiaolyuh.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author yuxuan.wu
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("api")
                .apiInfo(metaData())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.xiaolyuh.controller"))
                .paths(PathSelectors.any())
                .build();

    }

    private ApiInfo metaData() {

        return new ApiInfoBuilder()
                .title("API文档")
                .description("描述")
                .termsOfServiceUrl("")
                .contact(new Contact("yuhao.wang", "", ""))
                .version("1.0")
                .build();
    }
}
