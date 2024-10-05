package com.bulq.logistics.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "bulq_logistics",
        version = "Version 1.1",
        contact = @Contact(
            name = "Bulq", email = "bulqcommerce@gmail.com", url = "bulq.com"
        ),
        license = @License(
            name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0"
        ),
        termsOfService = "https://www.bulq.com/tos",
        description = "Spring Boot Restful API demo for bulq logistics operations"
    )
)
public class SwaggerConfig {
    
}
