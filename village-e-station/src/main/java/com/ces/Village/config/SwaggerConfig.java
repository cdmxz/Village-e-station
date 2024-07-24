package com.ces.Village.config;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 1. swagger配置类
 */
@Log4j2
@Configuration
@EnableSwagger2
@EnableWebMvc
public class SwaggerConfig {
    @Bean
    public Docket docket() {
        log.info("准备生成接口文档...");
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .groupName("移动端接口")
                .apiInfo(apiInfo())
                // 是否启用swagger
                .enable(true)
                .select()
                //指定生成接口需要扫描的包
                .apis(RequestHandlerSelectors.basePackage("com.ces.Village"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //设置文档标题(API名称)
                .title("乡村e站项目接口文档")
                //文档描述
                .description("乡村e站项目接口文档")
                //服务条款URL
                .termsOfServiceUrl("http://localhost:8080/")
                //版本号
                .version("1.0.0")
                .build();
    }
}
