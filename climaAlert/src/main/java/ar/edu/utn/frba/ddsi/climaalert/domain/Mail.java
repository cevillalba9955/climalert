package ar.edu.utn.frba.ddsi.climaalert.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Mail {
  String origen;
  String destino;
  String asunto;
  String mensaje;

  @Override
  public String toString() {
    return "Mail enviado{" +
        "origen='" + origen + '\'' +
        ", destino='" + destino + '\'' +
        ", asunto='" + asunto + '\'' +
        ", mensaje='" + mensaje + '\'' +
        '}';
  }
}
