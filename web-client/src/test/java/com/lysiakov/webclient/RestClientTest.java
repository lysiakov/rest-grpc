package com.lysiakov.webclient;

import com.lysiakov.shared.dataloader.RestPayloadHolder;
import com.lysiakov.shared.rest.dto.PayloadSize;
import com.lysiakov.shared.rest.dto.RestMeteoriteLanding;
import com.lysiakov.shared.rest.dto.SizeResponse;
import com.lysiakov.webclient.rest.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class RestClientTest {

    @Autowired
    private RestClient restClient;

    @Autowired
    private RestPayloadHolder payloadDataHolderRest;

    @Test
    void getSmallPayload() {
        List<RestMeteoriteLanding> payload = restClient.getPayload(PayloadSize.SMALL);
        Assertions.assertNotNull(payload);
        Assertions.assertEquals(1, payload.size());
        Assertions.assertEquals(payloadDataHolderRest.smallPayload(), payload);
    }

    @Test
    void getMediumPayload() {
        List<RestMeteoriteLanding> payload = restClient.getPayload(PayloadSize.MEDIUM);
        Assertions.assertNotNull(payload);
        Assertions.assertEquals(100, payload.size());
        Assertions.assertEquals(payloadDataHolderRest.mediumPayload(), payload);
    }

    @Test
    void getLargePayload() {
        List<RestMeteoriteLanding> payload = restClient.getPayload(PayloadSize.LARGE);
        Assertions.assertNotNull(payload);
        Assertions.assertEquals(1000, payload.size());
        Assertions.assertEquals(payloadDataHolderRest.largePayload(), payload);
    }

    @Test
    void sendPayload() {
        SizeResponse sizeResponse = restClient.sendPayload(payloadDataHolderRest.largePayload());
        Assertions.assertNotNull(sizeResponse);
        Assertions.assertEquals(payloadDataHolderRest.largePayload().size(), sizeResponse.getSize());
    }
}
