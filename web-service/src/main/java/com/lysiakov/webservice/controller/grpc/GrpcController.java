package com.lysiakov.webservice.controller.grpc;

import com.lysiakov.shared.dataloader.GrpcPayloadHolder;
import com.lysiakov.shared.grpc.GrpcMeteoriteLanding;
import com.lysiakov.shared.grpc.GrpcMeteoriteLandingList;
import com.lysiakov.shared.grpc.MeteoriteLandingsServiceGrpc;
import com.lysiakov.shared.grpc.PayloadRequest;
import com.lysiakov.shared.grpc.SizeResponse;
import io.grpc.stub.StreamObserver;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GrpcController extends MeteoriteLandingsServiceGrpc.MeteoriteLandingsServiceImplBase {

  private final GrpcPayloadHolder payloadHolder;

  @Override
  public void getPayload(PayloadRequest request,
      StreamObserver<GrpcMeteoriteLandingList> responseObserver) {
    List<GrpcMeteoriteLanding> payload = switch (request.getSize()) {
      case SMALL -> payloadHolder.smallPayload();
      case MEDIUM -> payloadHolder.mediumPayload();
      case LARGE -> payloadHolder.largePayload();
      case UNRECOGNIZED -> Collections.emptyList();
    };
    responseObserver.onNext(
        GrpcMeteoriteLandingList.newBuilder().addAllMeteoriteLandings(payload).build());
    responseObserver.onCompleted();
  }

  @Override
  public void acceptPayload(GrpcMeteoriteLandingList request,
      StreamObserver<SizeResponse> responseObserver) {
    SizeResponse sizeResponse = SizeResponse.newBuilder()
        .setSize(request.getMeteoriteLandingsList().size()).build();
    responseObserver.onNext(sizeResponse);
    responseObserver.onCompleted();
  }
}
