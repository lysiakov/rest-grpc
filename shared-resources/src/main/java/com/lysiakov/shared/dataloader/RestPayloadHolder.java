package com.lysiakov.shared.dataloader;

import com.lysiakov.shared.rest.dto.RestMeteoriteLanding;
import java.util.Collections;
import java.util.List;
import org.springframework.util.Assert;

public record RestPayloadHolder(
    List<RestMeteoriteLanding> smallPayload,
    List<RestMeteoriteLanding> mediumPayload,
    List<RestMeteoriteLanding> largePayload) {

  public RestPayloadHolder(
      List<RestMeteoriteLanding> smallPayload,
      List<RestMeteoriteLanding> mediumPayload,
      List<RestMeteoriteLanding> largePayload) {
    Assert.notEmpty(smallPayload, () -> "Payload collection is empty!");
    Assert.notEmpty(mediumPayload, () -> "Payload collection is empty!");
    Assert.notEmpty(largePayload, () -> "Payload collection is empty!");
    this.smallPayload = Collections.unmodifiableList(smallPayload);
    this.mediumPayload = Collections.unmodifiableList(mediumPayload);
    this.largePayload = Collections.unmodifiableList(largePayload);
  }
}
