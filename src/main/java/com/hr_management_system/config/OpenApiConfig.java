package com.hr_management_system.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI hrManagementOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("HR Management System API")
                        .description("API for managing employees, departments, positions, and payroll")
                        .version("1.0.0"));
    }
}
