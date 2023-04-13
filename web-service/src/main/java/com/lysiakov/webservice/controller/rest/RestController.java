package com.lysiakov.webservice.controller.rest;

import com.lysiakov.shared.dataloader.RestPayloadHolder;
import com.lysiakov.shared.rest.dto.PayloadSize;
import com.lysiakov.shared.rest.dto.RestMeteoriteLanding;
import com.lysiakov.shared.rest.dto.SizeResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@org.springframework.web.bind.annotation.RestController
@RequiredArgsConstructor
@RequestMapping("/rest")
public class RestController {

  private final RestPayloadHolder payloadDataHolderRest;

  @GetMapping
  public List<RestMeteoriteLanding> getPayload(@RequestParam("size") PayloadSize size) {
    return switch (size) {
      case SMALL -> payloadDataHolderRest.smallPayload();
      case MEDIUM -> payloadDataHolderRest.mediumPayload();
      case LARGE -> payloadDataHolderRest.largePayload();
    };
  }

  @PostMapping
  public SizeResponse acceptPayload(@RequestBody List<RestMeteoriteLanding> restMeteoriteLandings) {
    return SizeResponse.builder().size(restMeteoriteLandings.size()).build();
  }

}
