package com.lysiakov.webservice.controller.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GrpcConfig {

  private final GrpcController grpcController;

  @Bean
  public Server server() throws IOException, InterruptedException {
    Server server = ServerBuilder
        .forPort(8081)
        .addService(grpcController)
        .build();
    server.start();
    return server;
  }
}
