package com.example.projet_infra_3_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@EnableWebMvc
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