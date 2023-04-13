package com.lysiakov.shared.rest.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class RestMeteoriteLanding {

  private int id;
  private String name;
  private String nametype;
  private String recclass;
  private double mass;
  private Fall fall;
  private LocalDateTime year;
  private double reclat;
  private double reclong;
  private GeoLocation geolocation;
}
