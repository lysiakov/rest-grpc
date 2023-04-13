package com.lysiakov.webclient;

import com.lysiakov.shared.dataloader.GrpcPayloadHolder;

import com.lysiakov.shared.grpc.GrpcMeteoriteLanding;
import com.lysiakov.shared.grpc.PayloadSize;
import com.lysiakov.shared.grpc.SizeResponse;
import com.lysiakov.webclient.grpc.GrpcClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class GrpcClientTest {

    @Autowired
    private GrpcClient grpcClient;

    @Autowired
    private GrpcPayloadHolder grpcPayloadHolder;

    @Test
    void getSmallPayload() {
        List<GrpcMeteoriteLanding> response = grpcClient.getPayload(PayloadSize.SMALL);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(1, response.size());
        Assertions.assertEquals(grpcPayloadHolder.smallPayload(), response);
    }

    @Test
    void getMediumPayload() {
        List<GrpcMeteoriteLanding> response = grpcClient.getPayload(PayloadSize.MEDIUM);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(100, response.size());
        Assertions.assertEquals(grpcPayloadHolder.mediumPayload(), response);
    }

    @Test
    void getLargePayload() {
        List<GrpcMeteoriteLanding> response = grpcClient.getPayload(PayloadSize.LARGE);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(1000, response.size());
        Assertions.assertEquals(grpcPayloadHolder.largePayload(), response);
    }

    @Test
    void sendPayload() {
        SizeResponse sizeResponse = grpcClient.acceptPayload(grpcPayloadHolder.largePayload());
        Assertions.assertNotNull(sizeResponse);
        Assertions.assertEquals(grpcPayloadHolder.largePayload().size(), sizeResponse.getSize());
    }
}
