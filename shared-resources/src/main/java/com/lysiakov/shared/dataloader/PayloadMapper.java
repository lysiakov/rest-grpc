package com.lysiakov.shared.dataloader;

import com.google.protobuf.Timestamp;
import com.lysiakov.shared.grpc.Fall;
import com.lysiakov.shared.grpc.GeoLocation;
import com.lysiakov.shared.grpc.GeoLocationType;
import com.lysiakov.shared.grpc.GrpcMeteoriteLanding;
import com.lysiakov.shared.grpc.NullableGeoLocation;
import com.lysiakov.shared.rest.dto.RestMeteoriteLanding;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PayloadMapper {

  public List<GrpcMeteoriteLanding> toGrpcPayload(List<RestMeteoriteLanding> restMeteoriteLanding) {
    return restMeteoriteLanding.stream().map(this::toGrpcPayload).toList();
  }

  public GrpcMeteoriteLanding toGrpcPayload(RestMeteoriteLanding restMeteoriteLanding) {
    return GrpcMeteoriteLanding.newBuilder()
        .setId(restMeteoriteLanding.getId())
        .setName(restMeteoriteLanding.getName())
        .setFall(mapFallEnum(restMeteoriteLanding.getFall()))
        .setGeolocation(mapToGeoLocation(restMeteoriteLanding.getGeolocation()))
        .setMass(restMeteoriteLanding.getMass())
        .setNametype(restMeteoriteLanding.getNametype())
        .setRecclass(restMeteoriteLanding.getRecclass())
        .setReclat(restMeteoriteLanding.getReclat())
        .setReclong(restMeteoriteLanding.getReclong())
        .setYear(mapToTimestamp(restMeteoriteLanding.getYear()))
        .build();
  }

  private NullableGeoLocation mapToGeoLocation(
      com.lysiakov.shared.rest.dto.GeoLocation geoLocation) {
    NullableGeoLocation.Builder builder = NullableGeoLocation.newBuilder();
    if (geoLocation == null) {
      return builder.build();
    }
    GeoLocation grpcGeoLocation = GeoLocation.newBuilder()
        .setType(mapGeoLocationType(geoLocation.getType()))
        .addAllCoordinates(geoLocation.getCoordinates())
        .build();
    return builder.setData(grpcGeoLocation).build();
  }

  private Timestamp mapToTimestamp(LocalDateTime localDateTime) {
    Instant instant = localDateTime.toInstant(ZoneOffset.UTC);
    return Timestamp.newBuilder()
        .setSeconds(instant.getEpochSecond())
        .setNanos(instant.getNano())
        .build();
  }

  private Fall mapFallEnum(com.lysiakov.shared.rest.dto.Fall fall) {
    return switch (fall) {
      case FELL -> Fall.FELL;
      case FOUND -> Fall.FOUND;
    };
  }

  private GeoLocationType mapGeoLocationType(
      com.lysiakov.shared.rest.dto.GeoLocationType geoLocationType) {
    return switch (geoLocationType) {
      case POINT -> GeoLocationType.POINT;
    };
  }
}
