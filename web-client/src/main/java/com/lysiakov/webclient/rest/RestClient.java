package com.lysiakov.webclient.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lysiakov.shared.rest.dto.PayloadSize;
import com.lysiakov.shared.rest.dto.RestMeteoriteLanding;
import com.lysiakov.shared.rest.dto.SizeResponse;
import com.lysiakov.webclient.WebServiceProperties;
import java.io.IOException;
import java.util.List;
import okhttp3.HttpUrl;
import okhttp3.HttpUrl.Builder;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RestClient {

  private static final String HTTP_SCHEME = "http";
  private static final String REST_PATH_SEGMENT = "rest";
  private static final String SIZE_PARAMETER = "size";
  private final OkHttpClient okHttpClient;
  private final ObjectMapper objectMapper;
  private final HttpUrl webServiceUrl;

  @Autowired
  public RestClient(ObjectMapper objectMapper, WebServiceProperties webServiceProperties) {
    this.objectMapper = objectMapper;
    this.okHttpClient = new OkHttpClient();
    webServiceUrl = new Builder()
        .scheme(HTTP_SCHEME)
        .host(webServiceProperties.host())
        .port(webServiceProperties.restPort())
        .addPathSegment(REST_PATH_SEGMENT)
        .build();
  }

  public List<RestMeteoriteLanding> getPayload(PayloadSize payloadSize) {
    HttpUrl url = webServiceUrl.newBuilder()
        .addQueryParameter(SIZE_PARAMETER, payloadSize.toString())
        .build();
    Request request = new Request.Builder()
        .url(url)
        .build();
    String response = executeRequest(request);
    List<RestMeteoriteLanding> restMeteoriteLandings = List.of(
        parseResponse(response, RestMeteoriteLanding[].class));
    return restMeteoriteLandings;
  }

  public SizeResponse sendPayload(List<RestMeteoriteLanding> restMeteoriteLandings) {
    Request request = new Request.Builder()
        .url(webServiceUrl)
        .post(RequestBody.create(parseBody(restMeteoriteLandings),
            MediaType.parse("application/json")))
        .build();
    String response = executeRequest(request);
    return parseResponse(response, SizeResponse.class);
  }

  private <T> T parseResponse(String response, Class<T> clazz) {
    try {
      return objectMapper.readValue(response, clazz);
    } catch (Exception ex) {
      throw new RuntimeException("Failed to parse response from service");
    }
  }

  private String parseBody(List<RestMeteoriteLanding> restMeteoriteLandings) {
    try {
      return objectMapper.writeValueAsString(restMeteoriteLandings);
    } catch (IOException ex) {
      throw new RuntimeException("Failed to prepare payload data");
    }
  }

  private String executeRequest(Request request) {
    try (Response response = okHttpClient.newCall(request).execute()) {
      return response.body().string();
    } catch (IOException ex) {
      throw new RuntimeException("Failed to call rest client class");
    }
  }
}
