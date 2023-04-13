package com.lysiakov.webclient.performance;

import com.lysiakov.shared.dataloader.GrpcPayloadHolder;
import com.lysiakov.shared.dataloader.RestPayloadHolder;
import com.lysiakov.shared.rest.dto.PayloadSize;
import com.lysiakov.webclient.grpc.GrpcClient;
import com.lysiakov.webclient.rest.RestClient;
import java.time.Instant;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PerformanceTest extends AbstractPerformanceTest {

  @Autowired
  private RestClient restClient;

  @Autowired
  private RestPayloadHolder restPayloadHolderl;

  @Autowired
  private GrpcClient grpcClient;

  @Autowired
  private GrpcPayloadHolder grpcPayloadHolder;

  public PerformanceTest() {
    super(1, 1);
  }

  @RepeatedTest(2)
  public void getPayloadRest() throws Exception {
    System.out.println(" Test execution start at: " + Instant.now());
    runWebClientCallConcurrently("Get payload REST API", () -> restClient.getPayload(PayloadSize.MEDIUM));
    System.out.println(" Test execution ends at: " + Instant.now());
  }

  @RepeatedTest(2)
  public void getPayloadGrpc() throws Exception {
    System.out.println(" Test execution start at: " + Instant.now());
    runWebClientCallConcurrently("Get payload gRPC API", () -> grpcClient.getPayload(
        com.lysiakov.shared.grpc.PayloadSize.MEDIUM));
    System.out.println(" Test execution ends at: " + Instant.now());
  }

  @Test
  public void sendPayloadRest() throws Exception {
    System.out.println(" Test execution start at: " + Instant.now());
    runWebClientCallConcurrently("Get payload REST API", () -> restClient.sendPayload(restPayloadHolderl.mediumPayload()));
    System.out.println(" Test execution ends at: " + Instant.now());
  }

  @Test
  public void sendPayloadGrpc() throws Exception {
    System.out.println(" Test execution start at: " + Instant.now());
    runWebClientCallConcurrently("Get payload gRPC API", () -> grpcClient.acceptPayload(grpcPayloadHolder.largePayload()));
    System.out.println(" Test execution ends at: " + Instant.now());
  }
}
