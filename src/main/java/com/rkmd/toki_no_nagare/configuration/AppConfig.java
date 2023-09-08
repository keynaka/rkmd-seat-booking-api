package com.rkmd.toki_no_nagare.configuration;

import ch.qos.logback.access.servlet.TeeFilter;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.modelmapper.ModelMapper;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableScheduling
public class AppConfig implements WebMvcConfigurer {

  /** Model Mapper configuration */
  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }

  /** Logback configuration */
  @Bean
  public FilterRegistrationBean<TeeFilter> logbackAccessFilter() {

    FilterRegistrationBean<TeeFilter> filterRegBean = new FilterRegistrationBean<>();
    TeeFilter filter = new TeeFilter();

    filterRegBean.setFilter(filter);
    filterRegBean.addUrlPatterns("/*");

    return filterRegBean;
  }

  /** Swagger configuration */
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

  /** CORS configuration*/
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOrigins("*")
        .allowedMethods("GET", "POST", "PUT", "DELETE")
        .allowedHeaders("*");
  }

}
