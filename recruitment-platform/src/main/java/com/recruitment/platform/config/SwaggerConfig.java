package com.recruitment.platform.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI/Swagger configuration.
 *
 * Registers a single reusable "bearerAuth" security scheme so every controller
 * can opt in via @SecurityRequirement(name = "bearerAuth") instead of redefining
 * the JWT scheme per-endpoint. Applied globally as a default requirement; public
 * endpoints (auth, public job browsing) override it with @SecurityRequirements()
 * at the controller level - see AuthController and PublicJobController.
 */
@Configuration
public class SwaggerConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI recruitmentPlatformOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("AI Recruitment Management System API")
                        .description("Production-grade backend API for candidate/recruiter/admin recruitment workflows, "
                                + "with AI-powered resume analysis and job matching.")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Backend Engineering Team")
                                .email("engineering@recruitmentplatform.com")))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .name(SECURITY_SCHEME_NAME)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Provide the JWT access token obtained from /api/v1/auth/login")));
    }
}