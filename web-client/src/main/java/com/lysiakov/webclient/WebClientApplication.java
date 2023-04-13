package com.lysiakov.webclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.lysiakov.shared", "com.lysiakov.webclient"})
@EnableConfigurationProperties(WebServiceProperties.class)
public class WebClientApplication {

  public static void main(String[] args) {
    SpringApplication.run(WebClientApplication.class, args);
  }
}
