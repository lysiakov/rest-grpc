package com.lysiakov.webservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.lysiakov.shared", "com.lysiakov.webservice"})
public class WebServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(WebServiceApplication.class, args);
  }
}