package com.example.hairsalon.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class OpenAPIConfig {
        @Value("${app.openapi.dev-url}")
        private String devUrl;

        @Value("${app.openapi.prod-url}")
        private String prodUrl;


        @Bean
        public OpenAPI myOpenAPI() {
                Server devServer = new Server();
                devServer.setUrl(devUrl);
                devServer.setDescription("Server URL in Development environment");

                Server prodServer = new Server();
                prodServer.setUrl(prodUrl);
                prodServer.setDescription("Server URL in Production environment");


                return new OpenAPI().servers(List.of(devServer, prodServer))
                        .addSecurityItem(new SecurityRequirement().
                                addList("Bearer Authentication"))
                        .components(new Components().addSecuritySchemes
                                ("Bearer Authentication", createAPIKeyScheme()));
        }

        private io.swagger.v3.oas.models.security.SecurityScheme createAPIKeyScheme() {
                return new io.swagger.v3.oas.models.security.SecurityScheme().type(SecurityScheme.Type.HTTP)
                        .bearerFormat("JWT")
                        .scheme("bearer");
        }
}