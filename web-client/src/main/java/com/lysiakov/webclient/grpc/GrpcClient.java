package com.lysiakov.webclient.grpc;

import com.lysiakov.shared.grpc.GrpcMeteoriteLanding;
import com.lysiakov.shared.grpc.GrpcMeteoriteLandingList;
import com.lysiakov.shared.grpc.MeteoriteLandingsServiceGrpc;
import com.lysiakov.shared.grpc.PayloadRequest;
import com.lysiakov.shared.grpc.PayloadSize;
import com.lysiakov.shared.grpc.SizeResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.List;
import org.springframework.stereotype.Component;


@Component
public class GrpcClient {

  private final MeteoriteLandingsServiceGrpc.MeteoriteLandingsServiceBlockingStub meteoriteService;

  public GrpcClient() {
    ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8081)
        .usePlaintext()
        .build();
    meteoriteService = MeteoriteLandingsServiceGrpc.newBlockingStub(channel);
  }

  public List<GrpcMeteoriteLanding> getPayload(PayloadSize size) {
    PayloadRequest request = PayloadRequest.newBuilder().setSize(size).build();
    return meteoriteService.getPayload(request).getMeteoriteLandingsList();
  }

  public SizeResponse acceptPayload(List<GrpcMeteoriteLanding> meteoriteLandings) {
    GrpcMeteoriteLandingList request = GrpcMeteoriteLandingList.newBuilder()
        .addAllMeteoriteLandings(meteoriteLandings)
        .build();
    return meteoriteService.acceptPayload(request);
  }
}
