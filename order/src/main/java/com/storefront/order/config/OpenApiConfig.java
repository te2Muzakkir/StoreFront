package com.storefront.order.config;

import java.util.List;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Order",
                description = "REST API documentation for the Order Service",
                contact = @Contact(
                        name = "Storefront Team",
                        email = "support@example.com"
                )
        ),
        security = @SecurityRequirement(name = "Bearer Authentication")
)
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {

    @Bean
    public OpenApiCustomizer versionCustomizer(BuildProperties buildProperties) {
    	return openApi -> {
    		if (openApi.getInfo() != null) 
    			openApi.getInfo().setVersion(buildProperties.getVersion());
    		openApi.setServers(List.of(new Server().url("/storefront/order")));
    	};
    }

}