package ar.edu.utn.frba.ddsi.climaalert.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WeatherResponseDto{
  private long lastUpdateEpoch;
  private String fechaHora;
  private double temperatura;
  private double humedad;
}


