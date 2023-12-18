package com.example.projet_infra_3_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.example.projet_infra_3_backend.constant.SwaggerRoot.APP_ROOT;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(new ApiInfoBuilder()
                .description("Gestion des user API documentation")
                .title("Gestion des User API")
                .build())
                .groupName("REST API V1")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.projet_infra_3_backend.controller"))
                .paths(PathSelectors.any())
                .build();
    }
}