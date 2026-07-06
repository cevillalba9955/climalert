package ar.edu.utn.frba.ddsi.climaalert.domain;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class Clima {
  private long lastUpdateEpoch;
  private LocalDateTime fechaHora;
  private double temperatura;
  private double humedad;
}
