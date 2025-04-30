package com.example.library.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
// Aqui es para customizar el OpenAPI/Swagger
    @Bean
    public OpenAPI nuestoCustomModo() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
        // esto es simplemente lo que sale arriba de la pagina
                .info(new Info()
                        .title("El mejor Library API")
                        .version("1.0")
                        .description("Documentacion."))
                        // Aqui decimos que se use el bearerAuth
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                // aqui avisamos de que se espera 
                .components(
                    new Components()
                        .addSecuritySchemes(securitySchemeName,
                            new SecurityScheme()
                                .name("Authorization")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT"))
                                
                );
    }

    // Aqui simplemente documentamos los endpoints 
    @Bean
    public GroupedOpenApi apiPublico() {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/**")
                .addOpenApiCustomizer(openApi -> {
                    
                })
                .build();
    }
}