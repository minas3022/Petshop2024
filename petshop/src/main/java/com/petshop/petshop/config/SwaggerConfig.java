package com.petshop.petshop.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi petShopApi() {
        return GroupedOpenApi.builder()
                .group("pet-shop-api")
                .pathsToMatch("/**") // Todos os endpoints da API
                .build();
    }
}
