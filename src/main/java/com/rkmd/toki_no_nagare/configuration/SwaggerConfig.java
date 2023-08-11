package com.rkmd.toki_no_nagare.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  OpenAPI customOpenAPI() {
    Contact contact = new Contact();
    contact.setName("to: Matsuri Daiko");
    contact.setEmail("info@matsuridaiko.com.ar");

    return new OpenAPI().info(
        new Info()
            .title("Toki No Nagare Api")
            .description("Servicio de reservas de entradas para el evento: Toki No Nagare")
            .version("0.0.1")
            .contact(contact));
  }

}
