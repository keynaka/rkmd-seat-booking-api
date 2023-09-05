package com.rkmd.toki_no_nagare.configuration;

import ch.qos.logback.access.servlet.TeeFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogbackAccessConfig {

  @Bean
  public FilterRegistrationBean<TeeFilter> logbackAccessFilter() {

    FilterRegistrationBean<TeeFilter> filterRegBean = new FilterRegistrationBean<>();
    TeeFilter filter = new TeeFilter();

    filterRegBean.setFilter(filter);
    filterRegBean.addUrlPatterns("/*");

    return filterRegBean;
  }
}
