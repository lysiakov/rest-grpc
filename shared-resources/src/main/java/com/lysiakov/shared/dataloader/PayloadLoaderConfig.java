package com.lysiakov.shared.dataloader;

import com.fasterxml.jackson.databind.MapperFeature;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PayloadLoaderConfig {

  @Bean
  public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
    return builder -> builder
        .featuresToEnable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
        .failOnUnknownProperties(false);
  }
}
