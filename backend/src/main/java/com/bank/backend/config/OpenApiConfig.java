package com.bank.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración de OpenAPI/Swagger para documentación automática de la API
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sistema de Gestión de Aprobaciones API")
                        .version("1.0.0")
                        .description("API REST para centralizar flujos de aprobación genéricos en el CoE de desarrollo")
                        .contact(new Contact()
                                .name("Equipo de Desarrollo")
                                .email("dev@bank.com"))
                        .license(new License()
                                .name("Derechos Reservados BdB")
                                .url("https://bank.com")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080/api")
                                .description("Servidor de desarrollo local"),
                        new Server()
                                .url("http://backend:8080/api")
                                .description("Servidor Docker")
                ));
    }
}