package com.lysiakov.shared.rest.dto;

import java.util.List;
import lombok.Data;


@Data
public class GeoLocation {

  private GeoLocationType type;
  private List<Double> coordinates;

}
