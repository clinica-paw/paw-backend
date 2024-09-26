package com.clinicapaw.backend_clinicapaw.configuration.app;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "API Clínica Paw",
                description = "An API to manage the employees and clients of a veterinary clinic.",
                termsOfService = "www.clinica-paw-api.com/terminals_and_services",
                version = "1.0.0",
                contact = @Contact(
                        name = "Israel",
                        url = "www.clinica-paw-api.com",
                        email = "israel.bastion123@gmail.com"
                ),
                license = @License(
                        name = "Standard Software use License for me",
                        url = "www.clinica-paw-api.com/license"
                )
        ),
        servers = {
                @Server(
                        description = "dev server",
                        url = "http://localhost:8080"
                ),
                @Server(
                        description = "prod server",
                        url = "https://clinica-paw-api:8080"
                ),
        }
)
public class SwaggerConfig {
}
