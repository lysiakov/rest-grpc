package com.lysiakov.webclient;

import java.util.Objects;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("webservice")
public record WebServiceProperties(
    String host,
    Integer restPort,
    Integer grpcPort) {

  public WebServiceProperties(String host, Integer restPort, Integer grpcPort) {
    Objects.requireNonNull(host, "host is required");
    Objects.requireNonNull(restPort, "rest port is required");
    Objects.requireNonNull(grpcPort, "grpc port is required");
    this.host = host;
    this.restPort = restPort;
    this.grpcPort = grpcPort;
  }
}
