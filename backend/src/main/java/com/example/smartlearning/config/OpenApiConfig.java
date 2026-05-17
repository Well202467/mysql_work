package com.example.smartlearning.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI smartLearningOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("智慧学习平台后端接口文档")
                        .description("专业、课程、职业和管理员后台管理相关接口")
                        .version("1.0.0"));
    }
}
