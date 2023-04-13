package com.lysiakov.shared.dataloader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lysiakov.shared.grpc.GrpcMeteoriteLanding;
import com.lysiakov.shared.rest.dto.RestMeteoriteLanding;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@RequiredArgsConstructor
public class PayloadLoader implements BeanFactoryAware {

  @Value("classpath:small-payload.json")
  private Resource smallPayloadResource;
  @Value("classpath:medium-payload.json")
  private Resource mediumPayloadResource;
  @Value("classpath:large-payload.json")
  private Resource largePayloadResource;

  private ConfigurableBeanFactory beanFactory;
  private final ObjectMapper objectMapper;
  private final PayloadMapper payloadMapper;

  @Override
  public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    this.beanFactory = (ConfigurableBeanFactory) beanFactory;
  }

  @PostConstruct
  private void createPayloadHolderBean() throws IOException {
    try (var smallPayloadResourceInputStream = smallPayloadResource.getInputStream();
        var mediumPayloadResourceInputStream = mediumPayloadResource.getInputStream();
        var largePayloadResourceInputStream = largePayloadResource.getInputStream()) {

      List<RestMeteoriteLanding> restSmallPayload = objectMapper.readValue(
          smallPayloadResourceInputStream, new TypeReference<>() {
          });
      List<RestMeteoriteLanding> restMediumPayload = objectMapper.readValue(
          mediumPayloadResourceInputStream, new TypeReference<>() {
          });
      List<RestMeteoriteLanding> restLargePayload = objectMapper.readValue(
          largePayloadResourceInputStream, new TypeReference<>() {
          });
      RestPayloadHolder restDataHolder = new RestPayloadHolder(restSmallPayload, restMediumPayload,
          restLargePayload);
      beanFactory.registerSingleton(RestPayloadHolder.class.getCanonicalName(), restDataHolder);

      List<GrpcMeteoriteLanding> grpcSmallPayload = payloadMapper.toGrpcPayload(restSmallPayload);
      List<GrpcMeteoriteLanding> grpcMediumPayload = payloadMapper.toGrpcPayload(restMediumPayload);
      List<GrpcMeteoriteLanding> grpcLargePayload = payloadMapper.toGrpcPayload(restLargePayload);
      GrpcPayloadHolder grpcDataHolder = new GrpcPayloadHolder(grpcSmallPayload, grpcMediumPayload,
          grpcLargePayload);
      beanFactory.registerSingleton(GrpcPayloadHolder.class.getCanonicalName(), grpcDataHolder);
    }
  }
}
