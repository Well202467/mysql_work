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
                        .title("Smart Learning Backend API")
                        .description("Major, course, and knowledge point query APIs")
                        .version("1.0.0"));
    }
}
