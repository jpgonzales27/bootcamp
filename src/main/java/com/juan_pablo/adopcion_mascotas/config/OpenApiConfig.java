package com.juan_pablo.adopcion_mascotas.config;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Mascotas API",
                version = "1.0",
                description = "Documentation for endpoints in adoptions pets")
)
public class OpenApiConfig {
}
