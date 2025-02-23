package com.zipple.common.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    private static final String SECURITY_SCHEME_NAME = "Authorization";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info());
    }

    @Bean
    public GroupedOpenApi groupedOpenApi() {
        return GroupedOpenApi.builder()
                .group("default")
                .packagesToScan("com.zipple")
                .addOpenApiCustomizer(customizeGroupedApi())
                .build();
    }

    private OpenApiCustomizer customizeGroupedApi() {
        return openApi -> {
            openApi.info(new Info()
                            .title("Zipple API")
                            .description("공인 중개 서비스 API 문서")
                            .version("v1.0.0")
                            .contact(new Contact()
                                    .name("Donghwi")
                                    .email("tnqlsdld0222@gmail.com")))
                    .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                    .components(new Components()
                            .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
                                    .type(SecurityScheme.Type.HTTP)
                                    .scheme("bearer")
                                    .bearerFormat("JWT")
                                    .in(SecurityScheme.In.HEADER)
                                    .name(SECURITY_SCHEME_NAME)));
        };
    }
}
