package com.example.demo.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Password Manager API",
        version = "1.0",
        description = "API documentation for the Password Manager application. JWT token required for routes. All data will be encrypted in the database and on requests.",
        contact = @Contact(name = "John Arocha", email = "john.arocha@clover.com"),
        license = @License(name = "Apache 2.0", url = "http://springdoc.org")
    ),
    tags = {
        @Tag(name = "User Management", description = "Operations related to user management"),
        @Tag(name = "Login Credential Management", description = "Operations related to login credential management")
    }
)
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new io.swagger.v3.oas.models.info.Info()
                      .title("Password Manager API")
                      .version("1.0")
                      .description("API documentation for the Password Manager application. All data will be encrypted in the database and on requests.")
                      .contact(new io.swagger.v3.oas.models.info.Contact()
                                   .name("John Arocha")
                                   .email("john.arocha@clover.com"))
                      .license(new io.swagger.v3.oas.models.info.License()
                                   .name("Apache 2.0")
                                   .url("http://springdoc.org")))
            .externalDocs(new ExternalDocumentation()
                              .description("Password Manager Wiki Documentation (Not yet implemented)")
                              .url("http://example.com/wiki"));
    }
}