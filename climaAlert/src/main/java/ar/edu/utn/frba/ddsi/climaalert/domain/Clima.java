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
  private String condicion;
  private double velocidadViento;
  private double direccionViento;
  private double precipitacion;
  private double nubosidad;
  private double sensacionTermica;//    "feelslike_c": 5.1,
  private double puntoRocio; //        "dewpoint_c": 4.4,
  private double visibilidad; //        "vis_km": 4.0,
  private double radiacionUV; //       "uv": 0.0,
  private double lluvia  ; //      "will_it_rain": 0,
  private double probabilidadLluvia; //        "chance_of_rain": 43,
  private double nieve;       // "will_it_snow": 0,
  private double probabilidadNieve;//        "chance_of_snow": 0
}
