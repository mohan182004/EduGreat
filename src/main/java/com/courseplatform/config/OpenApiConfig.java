package com.courseplatform.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI(HttpServletRequest request) {
        String scheme = request.getHeader("X-Forwarded-Proto") != null
                ? request.getHeader("X-Forwarded-Proto") : request.getScheme();
        String host = request.getHeader("X-Forwarded-Host") != null
                ? request.getHeader("X-Forwarded-Host") : request.getServerName() + ":" + request.getServerPort();
        String serverUrl = scheme + "://" + host;

        return new OpenAPI()
                .servers(List.of(new Server().url(serverUrl).description("Current Server")))
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
