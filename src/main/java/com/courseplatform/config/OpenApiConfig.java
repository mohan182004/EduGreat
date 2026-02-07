package com.courseplatform.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Course Platform API")
                        .description("Backend service for a learning platform. " +
                                "Browse courses, search content, enroll, and track progress.\n\n" +
                                "**Public APIs** (no auth required): Course browsing, Search\n\n" +
                                "**Authenticated APIs** (JWT required): Enrollment, Progress tracking\n\n" +
                                "To authenticate: Register → Login → Copy token → Click 'Authorize' → Paste 'Bearer <token>'")
                        .version("1.0.0"))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Enter your JWT token")));
    }
}
