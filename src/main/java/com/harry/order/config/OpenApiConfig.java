package com.harry.order.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * Configure OpenAPI (Swagger UI) to support JWT Bearer authentication.
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(title = "Order API", version = "1.0", description = "Demo project with JWT security"),
        security = @SecurityRequirement(name = "bearerAuth") // Apply security globally
)
@SecurityScheme(
        name = "bearerAuth", // must match above
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Enter your JWT token in the format: Bearer <token>"
)
public class OpenApiConfig {
}
