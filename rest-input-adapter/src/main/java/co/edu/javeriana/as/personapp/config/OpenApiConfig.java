package co.edu.javeriana.as.personapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("PersonAPP - Arquitectura Hexagonal")
						.version("1.0.0")
						.description("API REST para la gestión de Personas, Profesiones, Teléfonos y Estudios bajo Arquitectura Hexagonal con persistencia dual en MariaDB y MongoDB.")
						.contact(new Contact()
								.name("Laboratorio 2 - Arquitectura de Software")
								.email("estudiante@javeriana.edu.co"))
						.license(new License()
								.name("Apache 2.0")
								.url("https://www.apache.org/licenses/LICENSE-2.0")));
	}
}
