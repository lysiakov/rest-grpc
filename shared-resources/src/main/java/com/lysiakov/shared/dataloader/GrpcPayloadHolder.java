package com.lysiakov.shared.dataloader;

import com.lysiakov.shared.grpc.GrpcMeteoriteLanding;
import java.util.Collections;
import java.util.List;
import org.springframework.util.Assert;

public record GrpcPayloadHolder(
    List<GrpcMeteoriteLanding> smallPayload,
    List<GrpcMeteoriteLanding> mediumPayload,
    List<GrpcMeteoriteLanding> largePayload) {

  public GrpcPayloadHolder(List<GrpcMeteoriteLanding> smallPayload,
      List<GrpcMeteoriteLanding> mediumPayload, List<GrpcMeteoriteLanding> largePayload) {
    Assert.notEmpty(smallPayload, () -> "Payload collection is empty!");
    Assert.notEmpty(mediumPayload, () -> "Payload collection is empty!");
    Assert.notEmpty(largePayload, () -> "Payload collection is empty!");
    this.smallPayload = Collections.unmodifiableList(smallPayload);
    this.mediumPayload = Collections.unmodifiableList(mediumPayload);
    this.largePayload = Collections.unmodifiableList(largePayload);
  }
}
