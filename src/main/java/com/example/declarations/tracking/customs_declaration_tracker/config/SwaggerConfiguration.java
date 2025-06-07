package com.example.declarations.tracking.customs_declaration_tracker.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Customs Declaration Tracking API")
                        .version("1.0.0")
                        .description("API для отслеживания статусов таможенных деклараций"));
    }
}